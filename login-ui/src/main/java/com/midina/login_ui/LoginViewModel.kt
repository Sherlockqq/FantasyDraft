package com.midina.login_ui

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.GoogleSignInUsecase
import com.midina.login_domain.usecase.SigningInUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val signingInUsecase: SigningInUsecase,
    private val googleSignUsecase: GoogleSignInUsecase,
    private val coroutineDispatcher: CoroutineDispatcher
) :
    ViewModel() {

    private val _loginEvents = MutableStateFlow<LoginEvent>(LoginEvent.OnDefault)
    val loginEvents: StateFlow<LoginEvent>
        get() = _loginEvents.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String>
        get() = _password.asStateFlow()

    init {
    }

    fun onEmailChanged(text: Editable?) {
        _email.value = text.toString()
    }

    fun onPasswordChanged(text: Editable?) {
        _password.value = text.toString()
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

    private suspend fun signIn() {
        val result = signingInUsecase.execute(_email.value, _password.value)
        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSuccess
            is ResultEvent.InvalidateData -> _loginEvents.value = LoginEvent.OnError
            is ResultEvent.Error -> {
            }
        }
    }

    private suspend fun googleSignIn(idToken: String) {
        val result = googleSignUsecase.execute(idToken)

        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSuccess
            is ResultEvent.InvalidateData -> _loginEvents.value = LoginEvent.OnError
            is ResultEvent.Error -> {}
        }
    }
}

sealed class LoginEvent {
    object OnSuccess : LoginEvent()
    object OnError : LoginEvent()
    object OnDefault : LoginEvent()
}