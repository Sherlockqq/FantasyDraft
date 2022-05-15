package com.midina.draft_data

import com.facebook.login.LoginManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.draft_domain.model.ResultEvent
import com.midina.draft_domain.model.ResultSending
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class SignedRepository {
    suspend fun isSigned(): ResultEvent<String> {

        return suspendCoroutine { continuation ->
            try {
                Firebase.auth.currentUser?.let { user ->
                    val email = user.email.toString()
                    user.reload().addOnSuccessListener {
                        if (user.isEmailVerified) {
                            continuation.resume(ResultEvent.Verified(email))
                        } else {
                            continuation.resume(ResultEvent.NotVerified(email))
                        }
                    }
                } ?: continuation.resume(ResultEvent.NotSigned)
            } catch (e: Exception) {
                continuation.resume(ResultEvent.NotSigned)
            }
        }
    }

    fun signedOut() {
        Firebase.auth.signOut()
        LoginManager.getInstance().logOut()
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
