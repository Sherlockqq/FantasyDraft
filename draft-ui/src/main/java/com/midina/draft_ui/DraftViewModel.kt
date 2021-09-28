package com.midina.draft_ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.core_ui.ui.SingleLiveEvent
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.usecase.IsSigned
import com.midina.draft_domain.usecase.SignedOut
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftViewModel  @Inject constructor(
    private val isSigned : IsSigned, private val signedOut: SignedOut): ViewModel(){

    private val _signEvents = SingleLiveEvent<SigningUiEvent>()
    val signEvents : LiveData<SigningUiEvent>
        get() = _signEvents

    init{
        viewModelScope.launch(Dispatchers.IO) {
            isSignedIn()
        }
    }

    private suspend fun isSignedIn(){
        val result = isSigned.execute()

        when (result){
            is ResultEvent.Signed -> _signEvents.postValue(SigningUiEvent.onSignIn)
            is ResultEvent.NotSigned -> _signEvents.postValue(SigningUiEvent.onNotSignIn)
        }
    }

   fun signedOutClicked(){
       viewModelScope.launch(Dispatchers.IO) {
           signingOut()
       }
   }

    private suspend fun signingOut(){
        signedOut.execute()
        _signEvents.postValue(SigningUiEvent.onNotSignIn)
    }
}

sealed class SigningUiEvent {
    object onSignIn : SigningUiEvent()
    object onNotSignIn : SigningUiEvent()
}
