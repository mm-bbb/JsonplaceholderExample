package com.example.jsonplaceholder.ui;

import com.example.jsonplaceholder.ui.MainModule;
import com.example.jsonplaceholder.viewmodel.di.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    /**
     * MainActivity를 위한 서브컴포넌트 정의한다.
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

}
