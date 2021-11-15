package com.example.fantasydraft

import android.app.Application
import com.example.fantasydraft.di.AppComponent
import com.example.fantasydraft.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class FantasydraftApplication : Application(), HasAndroidInjector {
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder().create(applicationContext).build()
        appComponent.inject(this)
        super.onCreate()
    }
}