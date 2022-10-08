package com.example.jsonplaceholder.ui.user;

import android.content.Context;
import android.view.LayoutInflater;

import com.example.jsonplaceholder.databinding.FragmentUserBinding;
import com.example.jsonplaceholder.viewmodel.di.ActivityContext;
import com.example.jsonplaceholder.viewmodel.di.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides
    @FragmentScope
    FragmentUserBinding provideBinding(@ActivityContext Context context){
        return FragmentUserBinding.inflate(LayoutInflater.from(context));
    }
}
