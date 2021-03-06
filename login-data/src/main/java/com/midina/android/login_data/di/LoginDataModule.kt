package com.midina.android.login_data.di

import com.midina.android.login_data.SigningInRepository
import com.midina.android.login_data.usecaseimpl.*
import com.midina.login_domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(
    includes = [
        LoginDataUseCaseModule::class
    ]
)
class LoginDataModule {
    @Provides
    @Singleton
    fun provideSigningInRepository() = SigningInRepository()

}

@Module
interface LoginDataUseCaseModule {
    @Binds
    fun bindSigningInImpl(signIn: SigningInUsecaseImpl): SigningInUsecase

    @Binds
    fun bindGoogleSignInImpl(googleSignIn: GoogleSignInUsecaseImpl): GoogleSignInUsecase

    @Binds
    fun bindFacebookSignInImpl(facebookSignIn: FacebookSignInUsecaseImpl): FacebookSignInUsecase

    @Binds
    fun bindCheckEmailExistImpl(checkEmailExistUsecase: CheckEmailExistUsecaseImpl): CheckEmailExistUsecase

    @Binds
    fun bindPasswordResetImpl(checkEmailExistUsecase: PasswordResetUsecaseImpl): PasswordResetUsecase
}