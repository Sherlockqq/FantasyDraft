package com.midina.draft_ui

import androidx.lifecycle.*
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.model.ResultSending
import com.midina.draft_domain.usecase.IsSignedUsecase
import com.midina.draft_domain.usecase.SendingVerificationUsecase
import com.midina.draft_domain.usecase.SignedOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val isSignedUsecase: IsSignedUsecase,
    private val signedOutUseCase: SignedOutUseCase,
    private val sendingVerificationUsecase: SendingVerificationUsecase
) : ViewModel(), LifecycleObserver {

    private val _signEvents = MutableStateFlow<SigningUiEvent>(SigningUiEvent.OnNotSignIn)
    val signEvents: StateFlow<SigningUiEvent>
        get() = _signEvents.asStateFlow()

    private val _sendingEvents = MutableStateFlow<SendingEvent>(SendingEvent.OnDefault)
    val sendingEvent: StateFlow<SendingEvent>
        get() = _sendingEvents.asStateFlow()


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun checkUser() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = isSignedUsecase.execute()
            when (result) {
                is ResultEvent.Verified -> _signEvents.value =
                    SigningUiEvent.OnVerified(result.email)
                is ResultEvent.NotSigned -> _signEvents.value = SigningUiEvent.OnNotSignIn
                is ResultEvent.NotVerified -> _signEvents.value =
                    SigningUiEvent.OnNotVerified(result.email)
            }
        }
    }

    fun signedOutClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            signingOut()
        }
    }

    fun verifyClicked() {
        viewModelScope.launch(Dispatchers.IO )  {
           val result = sendingVerificationUsecase.execute()
            when(result) {
                ResultSending.Error -> {
                    _sendingEvents.value = SendingEvent.OnError
                }
                ResultSending.Success -> {
                    _sendingEvents.value = SendingEvent.OnSuccess
                }
            }
        }
    }

    private suspend fun sendingVerification() {
    }

    private suspend fun signingOut() {
        signedOutUseCase.execute()
        _signEvents.value = SigningUiEvent.OnNotSignIn
    }
}

sealed class SigningUiEvent {
    class OnVerified(val email: String) : SigningUiEvent()
    class OnNotVerified(val email: String) : SigningUiEvent()
    object OnNotSignIn : SigningUiEvent()
}

sealed class SendingEvent {
    object OnSuccess : SendingEvent()
    object OnError : SendingEvent()
    object OnDefault : SendingEvent()
}
