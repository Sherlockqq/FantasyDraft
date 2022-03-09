package com.midina.draft_data

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.model.ResultSending
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class SignedRepository {
    fun isSigned(): ResultEvent<String> {
        val fAuth = Firebase.auth
        val currentUser = fAuth.currentUser
        Log.d("TAG", "USER EMAIL : ${currentUser?.email}")
        return if (currentUser == null) {
            ResultEvent.NotSigned
        } else {
            val email = currentUser.email.toString()
            if (currentUser.isEmailVerified) {
                ResultEvent.Verified(email)
            } else {
                ResultEvent.NotVerified(email)
            }
        }
    }

    fun signedOut() {
        Firebase.auth.signOut()
        Log.d("TAG", "USER EMAIL : ${Firebase.auth.currentUser?.email}")
    }

    suspend fun sendVerification(): ResultSending {
        return suspendCoroutine { continuation ->
             try {
                 val fAuth = Firebase.auth
                 fAuth.currentUser?.sendEmailVerification()
                     ?.addOnSuccessListener {
                        continuation.resume(ResultSending.Success)
                     }
                     ?.addOnFailureListener {
                         continuation.resume(ResultSending.Error)
                     }
             } catch (e: Exception) {
                 continuation.resume(ResultSending.Error)
             }
        }
    }
}
