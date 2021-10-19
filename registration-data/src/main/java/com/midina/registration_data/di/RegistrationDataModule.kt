package com.midina.registration_data.di

import android.content.Context
import androidx.room.Room
import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserDatabase
import com.midina.registration_data.usecaseimpl.RegisterUserUsecaseImpl
import com.midina.registration_domain.usecase.RegisterUserUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
    includes = [
        RegistrationDataUseCaseModule::class
    ]
)
class RegistrationDataModule {

    @Provides
    @Singleton
    fun provideRoom(appContext: Context): UserDatabase {
        return Room
            .databaseBuilder(appContext, UserDatabase::class.java, "database-user")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideRegisterUserRepository(user: UserDao) = RegisterUserRepository(user)
}

@Module
interface RegistrationDataUseCaseModule {
    @Binds
    fun bindRegisterUserImpl(registerUser: RegisterUserUsecaseImpl): RegisterUserUsecase
}

