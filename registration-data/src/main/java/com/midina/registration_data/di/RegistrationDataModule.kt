package com.midina.registration_data.di

import android.content.Context
import androidx.room.Room
import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserDatabase
import com.midina.registration_data.usecaseimpl.RegisterUserImpl
import com.midina.registration_domain.usecase.RegisterUser
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [
    RegistrationDataUseCaseModule::class
])
class RegistrationDataModule {

    @Provides
    @Singleton
    fun provideRoom(appContext: Context):UserDatabase{
        return Room
            .databaseBuilder(appContext,UserDatabase::class.java,"database-user")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase:UserDatabase): UserDao{
        return userDatabase.userDao()
    }

    @Provides
    @Singleton
    //TODO RESOLVE THIS ERROR!!!
    fun provideRegisterUserRepository() = RegisterUserRepository()
}

@Module
interface RegistrationDataUseCaseModule {
    @Binds
    fun bindRegisterUserImpl(registerUser: RegisterUserImpl): RegisterUser
}
