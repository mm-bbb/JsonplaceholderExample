package com.example.jsonplaceholder.viewmodel;

import android.app.Application;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jsonplaceholder.model.PostItem;
import com.example.jsonplaceholder.model.data.PostService;
import com.example.jsonplaceholder.viewmodel.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class PostViewModel extends AndroidViewModel implements PostItem.EventListener {

    @NonNull
    private final PostService postService;
    @NonNull
    private final SingleLiveEvent<Throwable> errorEvent;

    //RecyclerView에 표현할 아이템들을 LiveData로 관리
    private final MutableLiveData<List<PostItem>> livePosts = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    //게시글 아이템 클릭 이벤트를 관리
    private final SingleLiveEvent<PostItem> postClickEvent = new SingleLiveEvent<>();

    //뷰홀더용 뷰모델 리스트 변수
    private final List<PostItem> items = new ArrayList<>();
    @Inject
    public PostViewModel(@NonNull Application application,
                         PostService postService,
                         @Named("errorEvent") SingleLiveEvent<Throwable> errorEvent) {
        super(application);
        Timber.d("PostViewModel created");
        //오브젝트 그래프로 부터 생성자 주입
        this.postService = postService;
        this.errorEvent = errorEvent;
    }

    /**
     * 게시글 목록 불러오기
     */
    public void loadPosts() {
        compositeDisposable.add(postService.getPosts()
                .flatMapObservable(Observable::fromIterable)
                .map(post -> new PostItem(post, this))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(item -> loading.postValue(false))
                .subscribe(livePosts::setValue, errorEvent::setValue));
    }

    @NonNull
    public MutableLiveData<List<PostItem>> getLivePosts() {
        return livePosts;
    }

    public void observeLivePosts(@NonNull LifecycleOwner owner, RecyclerView.Adapter adapter){
        livePosts.observe(owner, list -> setItems(list, adapter));
    }

    public void observePostClickEvent(@NonNull LifecycleOwner owner, Lazy<NavController> navController, @NonNull NavDirections directions){
        postClickEvent.observe(owner, postItem ->
                navController.get().navigate(directions));
    }

    /**
     * ViewModel은 생명주기를 알고 동작한다.
     * 뷰모델이 파괴될 때, RxJava의 Disposable과 같은
     * 리소스 등을 해제할 수 있다록 한다.
     */

    @Override
    protected void onCleared() {
        super.onCleared();
        Timber.d("onCleared");
        compositeDisposable.dispose();
    }

    /**
     * PostItem 클릭 이벤트 구현
     */
    @Override
    public void onPostClick(PostItem postItem) {
        //Fragment로 이벤트를 전달하기 위해
        //SingleLiveEvent의 값을 변경한다.
        postClickEvent.setValue(postItem);
    }
    //PostFragment로 postClickEvent 변수를 노출
    public SingleLiveEvent<PostItem> getPostClickEvent() {
        return postClickEvent;
    }
    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    //외부로부터 게시글목록을 받아서 UI를 갱신한다.
    public void setItems(List<PostItem> items, RecyclerView.Adapter adapter) {
        this.items.clear();
        this.items.addAll(items);
        adapter.notifyDataSetChanged();
    }

    public List<PostItem> getItems() {
        return items;
    }
}
