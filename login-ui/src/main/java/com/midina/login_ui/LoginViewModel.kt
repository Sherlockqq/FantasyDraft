package com.midina.login_ui

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.SigningInUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val signingInUsecase: SigningInUsecase) :
    ViewModel() {

    private val _loginEvents = MutableStateFlow<LoginEvent>(LoginEvent.OnNotSigned)
    val loginEvents: StateFlow<LoginEvent>
        get() = _loginEvents.asStateFlow()

    private val _email = MutableStateFlow("")

    private val _password = MutableStateFlow("")

    init {
    }

    fun onEmailChanged(text: Editable?) {
        _email.value = text.toString()
    }

    fun onPasswordChanged(text: Editable?) {
        _password.value = text.toString()
    }

    fun signInClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            signIn()
        }
    }

    private suspend fun signIn() {
        val result = signingInUsecase.execute(_email.value, _password.value)
        when (result) {
            is ResultEvent.Success -> _loginEvents.value = LoginEvent.OnSigned
            is ResultEvent.InvalidateData -> _loginEvents.value = LoginEvent.OnNotSigned
            is ResultEvent.Error -> {
            }//TODO Exception
        }
    }
}

sealed class LoginEvent {
    object OnSigned : LoginEvent()
    object OnNotSigned : LoginEvent()
}