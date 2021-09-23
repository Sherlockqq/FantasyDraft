package com.midina.draft_ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class DraftViewModel  @Inject constructor(): ViewModel(){
    private val fAuth = Firebase.auth

    init{
        isSignedIn()
    }

    private fun isSignedIn(){

    }

}

sealed class SigningUiEvent {
    object onSignIn : SigningUiEvent()
    object onNotSignIn : SigningUiEvent()
}
