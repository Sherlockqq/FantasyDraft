package com.midina.login_ui

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.login_domain.model.EmailResult
import com.midina.login_domain.model.PasswordResetResult
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val signingInUsecase: SigningInUsecase,
    private val googleSignInUsecase: GoogleSignInUsecase,
    private val facebookSignInUsecase: FacebookSignInUsecase,
    private val checkEmailExistUsecase: CheckEmailExistUsecase,
    private val passwordResetUsecase: PasswordResetUsecase,
    private val coroutineDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val _loginEvents = MutableStateFlow<LoginEvent>(LoginEvent.OnDefault)
    val loginEvents: StateFlow<LoginEvent>
        get() = _loginEvents.asStateFlow()

    private val _resetEvents = MutableStateFlow<PasswordResetEvent>(PasswordResetEvent.OnDefault)
    val resetEvents: StateFlow<PasswordResetEvent>
        get() = _resetEvents.asStateFlow()

    private val _emailState = MutableStateFlow<State>(State.Default)
    val state: StateFlow<State>
        get() = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<State>(State.Default)
    val passwordState: StateFlow<State>
        get() = _passwordState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String>
        get() = _password.asStateFlow()

    fun onEmailChanged(text: Editable) {
        _email.value = text.toString()
        _emailState.value = State.Correct
    }

    fun onPasswordChanged(text: Editable) {
        _password.value = text.toString()
        _passwordState.value = State.Correct
    }

    fun emailFocusChanged() {
        viewModelScope.launch(coroutineDispatcher) {
            checkEmailExist()
        }
    }

    fun signInClicked() {
        viewModelScope.launch(coroutineDispatcher) {
            signIn()
        }
    }

    fun googleSignInClicked(idToken: String) {
        viewModelScope.launch(coroutineDispatcher) {
            googleSignIn(idToken)
        }
    }

    fun facebookSignInClicked(token: String) {
        viewModelScope.launch(coroutineDispatcher) {
            facebookSignIn(token)
        }
    }

    fun forgotPasswordClicked() {
        if (email.value.isEmpty()) {
            _emailState.value = State.Undefined
        } else {
            viewModelScope.launch(coroutineDispatcher) {
                resetPassword()
            }
        }
    }

    private suspend fun checkEmailExist() {
        val result = checkEmailExistUsecase.execute(email.value.trim())
        when (result) {
            EmailResult.Error -> _emailState.value = State.Undefined
            is EmailResult.Exist -> {
                _emailState.value = State.Correct
            }
            EmailResult.NotExist -> _emailState.value = State.Undefined
        }
    }

    private suspend fun signIn() {
        val result = signingInUsecase.execute(email.value.trim(), password.value)
        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSuccess
            is ResultEvent.InvalidateData -> _passwordState.value = State.Error
            is ResultEvent.Error -> _loginEvents.value = LoginEvent.OnError
        }
    }

    private suspend fun googleSignIn(idToken: String) {
        val result = googleSignInUsecase.execute(idToken)
        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSuccess
            is ResultEvent.InvalidateData -> _passwordState.value = State.Error
            is ResultEvent.Error -> _loginEvents.value = LoginEvent.OnError
        }
    }

    private suspend fun facebookSignIn(token: String) {
        val result = facebookSignInUsecase.execute(token)
        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSuccess
            is ResultEvent.InvalidateData -> _passwordState.value = State.Error
            is ResultEvent.Error -> _loginEvents.value = LoginEvent.OnError
        }
    }

    private suspend fun resetPassword() {
        val result = passwordResetUsecase.execute(email.value.trim())
        when (result) {
            PasswordResetResult.Error -> {
                _resetEvents.value = PasswordResetEvent.OnError
            }
            PasswordResetResult.Success -> {
                _resetEvents.value = PasswordResetEvent.OnSuccess
            }
            PasswordResetResult.InvalidateData -> {
                _emailState.value = State.Undefined
            }
        }
    }
}

sealed class LoginEvent {
    object OnSuccess : LoginEvent()
    object OnError : LoginEvent()
    object OnDefault : LoginEvent()
}

sealed class PasswordResetEvent {
    object OnSuccess : PasswordResetEvent()
    object OnError : PasswordResetEvent()
    object OnDefault : PasswordResetEvent()
}

sealed class State {
    object Undefined : State()
    object Correct : State()
    object Default : State()
    object Error : State()
}

