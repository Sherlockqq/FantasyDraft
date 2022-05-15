package com.midina.registration_data.di

import android.content.Context
import androidx.room.Room
import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserDatabase
import com.midina.registration_data.usecaseimpl.GenerateTokenUsecaseImpl
import com.midina.registration_data.usecaseimpl.RegisterUserUsecaseImpl
import com.midina.registration_data.usecaseimpl.WriteToFirebaseDatastoreUsecaseImpl
import com.midina.registration_data.usecaseimpl.WriteToRoomDatabaseUsecaseImpl
import com.midina.registration_domain.usecase.GenerateTokenUsecase
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToFirebaseDatastoreUsecase
import com.midina.registration_domain.usecase.WriteToRoomDatabaseUsecase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(
    includes = [
        UseCaseModule::class
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
    fun provideRegisterUserRepository(user: UserDao) =
        RegisterUserRepository(user)
}

@Module
interface UseCaseModule {
    @Binds
    fun bindRegisterUserImpl(registerUser: RegisterUserUsecaseImpl): RegisterUserUsecase

    @Binds
    fun bindWriteToRoomDatabaseImpl(roomDbUcImpl: WriteToRoomDatabaseUsecaseImpl):
            WriteToRoomDatabaseUsecase

    @Binds
    fun bindWriteToFirebaseDatastoreImpl(firebaseDsUcImpl: WriteToFirebaseDatastoreUsecaseImpl):
            WriteToFirebaseDatastoreUsecase

    @Binds
    fun bindGenerateToken(generateTokenUsecaseImpl: GenerateTokenUsecaseImpl): GenerateTokenUsecase
}

