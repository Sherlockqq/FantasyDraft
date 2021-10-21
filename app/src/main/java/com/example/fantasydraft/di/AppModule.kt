package com.example.fantasydraft.di

import com.example.fantasydraft.MainActivity
import dagger.Module

@Module
abstract class MainActivityModule {
    abstract fun providesMainActivity(): MainActivity
}
