package com.midina.draft_ui

import androidx.lifecycle.*
import com.midina.core_ui.ui.SingleLiveEvent
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.usecase.IsSignedUsecase
import com.midina.draft_domain.usecase.SignedOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftViewModel @Inject constructor(
    private val isSignedUsecase: IsSignedUsecase,
    private val signedOutUseCase: SignedOutUseCase
) : ViewModel(), LifecycleObserver {

    private val _signEvents = SingleLiveEvent<SigningUiEvent>()
    val signEvents: LiveData<SigningUiEvent>
        get() = _signEvents


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun isSignedIn() {
        viewModelScope.launch(Dispatchers.IO) {

            val result = isSignedUsecase.execute()
            when (result) {
                is ResultEvent.Signed -> _signEvents.postValue(SigningUiEvent.onSignIn)
                is ResultEvent.NotSigned -> _signEvents.postValue(SigningUiEvent.onNotSignIn)
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
        _signEvents.postValue(SigningUiEvent.onNotSignIn)
    }
}

sealed class SigningUiEvent {
    object onSignIn : SigningUiEvent()
    object onNotSignIn : SigningUiEvent()
}
