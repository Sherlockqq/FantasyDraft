package com.midina.android.login_data

import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.login_domain.model.ResultEvent

import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class SigningInRepository {

    suspend fun getIsSigned(email: String, password: String): ResultEvent {

        return suspendCoroutine { continuation ->
            try {
                val fAuth = Firebase.auth
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(ResultEvent.Success)
                    } else {
                        continuation.resume(ResultEvent.InvalidateData)
                    }
                }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error)
            }
        }
    }

    suspend fun googleSignIn(idToken: String): ResultEvent {
        return suspendCoroutine { continuation ->
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val fAuth = Firebase.auth
                fAuth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        continuation.resume(ResultEvent.Success)
                    }
                    .addOnFailureListener {
                        continuation.resume(ResultEvent.Error)
                }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error)
            }
        }
    }
}