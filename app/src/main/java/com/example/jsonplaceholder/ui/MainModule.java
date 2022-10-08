package com.example.jsonplaceholder.ui;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import com.example.jsonplaceholder.R;
import com.example.jsonplaceholder.databinding.ActivityMainBinding;
import com.example.jsonplaceholder.ui.detail.PostDetailFragment;
import com.example.jsonplaceholder.ui.detail.PostDetailModule;
import com.example.jsonplaceholder.ui.post.PostFragment;
import com.example.jsonplaceholder.ui.post.PostModule;
import com.example.jsonplaceholder.ui.user.UserFragment;
import com.example.jsonplaceholder.ui.user.UserModule;
import com.example.jsonplaceholder.viewmodel.di.ActivityContext;
import com.example.jsonplaceholder.viewmodel.di.ActivityScope;
import com.example.jsonplaceholder.viewmodel.di.FragmentScope;
import com.example.jsonplaceholder.ui.MainActivity;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @Provides
    @ActivityScope
    static ActivityMainBinding provideBinding(MainActivity activity) {
        return DataBindingUtil.setContentView(activity, R.layout.activity_main);
    }

    @Provides
    @ActivityContext
    static Context provideContext(MainActivity activity) {
        return activity;
    }

    /**
     * 서브컴포넌트 정의
     */
    @FragmentScope
    @ContributesAndroidInjector(modules = PostModule.class)
    abstract PostFragment getPostFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = PostDetailModule.class)
    abstract PostDetailFragment getPostDetailFragment();

    @FragmentScope
    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserFragment getUserFragment();
}

