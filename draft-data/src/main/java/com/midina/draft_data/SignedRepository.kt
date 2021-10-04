package com.midina.draft_data

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.draft_domain.model.ResultEvent
import javax.inject.Singleton

@Singleton
class SignedRepository {
    fun isSigned(): ResultEvent{
        val fAuth = Firebase.auth
        val currentUser = fAuth.currentUser
        Log.d("TAG","USER EMAIL : ${currentUser?.email}")
        return if (currentUser == null){
            ResultEvent.NotSigned
        }else{
            ResultEvent.Signed
        }
    }
    fun signedOut(){
        Firebase.auth.signOut()
        Log.d("TAG","USER EMAIL : ${Firebase.auth.currentUser?.email}")
    }
}
