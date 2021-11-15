package com.midina.draft_ui

import androidx.lifecycle.*
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.usecase.IsSignedUsecase
import com.midina.draft_domain.usecase.SignedOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val isSignedUsecase: IsSignedUsecase,
    private val signedOutUseCase: SignedOutUseCase
) : ViewModel(), LifecycleObserver {

    private val _signEvents = MutableStateFlow<SigningUiEvent>(SigningUiEvent.onNotSignIn)
    val signEvents: StateFlow<SigningUiEvent>
        get() = _signEvents.asStateFlow()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun isSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = isSignedUsecase.execute()
            when (result) {
                is ResultEvent.Signed -> _signEvents.value = SigningUiEvent.onSignIn
                is ResultEvent.NotSigned -> _signEvents.value = SigningUiEvent.onNotSignIn
            }
        }
    }

    fun signedOutClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            signingOut()
        }
    }

    private suspend fun signingOut() {
        signedOutUseCase.execute()
        _signEvents.value = SigningUiEvent.onNotSignIn
    }
}

sealed class SigningUiEvent {
    object onSignIn : SigningUiEvent()
    object onNotSignIn : SigningUiEvent()
}
