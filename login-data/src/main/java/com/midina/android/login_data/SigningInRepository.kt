package com.midina.android.login_data

import android.util.Log
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.midina.login_domain.model.EmailResult
import com.midina.login_domain.model.PasswordResetResult
import com.midina.login_domain.model.ResultEvent

import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TAG = "SigningInRepository"

@Singleton
class SigningInRepository {

    suspend fun getIsSigned(email: String, password: String): ResultEvent<String> {
        return suspendCoroutine { continuation ->
            try {
                val fAuth = Firebase.auth
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(ResultEvent.Success)
                    } else {
                        task.exception?.message?.let { message ->
                            ResultEvent.InvalidateData(
                                message
                            )
                        }?.let { result -> continuation.resume(result) }
                    }
                }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error)
            }
        }
    }

    suspend fun googleSignIn(idToken: String): ResultEvent<String> {
        return suspendCoroutine { continuation ->
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                Firebase.auth.signInWithCredential(credential)
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

    suspend fun facebookSignIn(token: String): ResultEvent<String> {
        return suspendCoroutine { continuation ->
            try {
                val credential = FacebookAuthProvider.getCredential(token)
                Firebase.auth.signInWithCredential(credential)
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

    suspend fun checkEmailExist(email: String): EmailResult {
        return suspendCoroutine { continuation ->
            try {
                Firebase.auth.fetchSignInMethodsForEmail(email)
                    .addOnSuccessListener { result ->
                        Log.d(TAG, "methods : ${result.signInMethods}")
                        if (result.signInMethods?.size == 0) {
                            continuation.resume(EmailResult.NotExist)
                        } else {
                            continuation.resume(EmailResult.Exist)
                        }
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "failure : $it")
                        continuation.resume(EmailResult.Error)
                    }
            } catch (e: Exception) {
                Log.d(TAG, "Catch : $e")
                continuation.resume(EmailResult.Error)
            }
        }
    }

    suspend fun resetPassword(email: String): PasswordResetResult {
        return suspendCoroutine { continuation ->
            try {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        continuation.resume(PasswordResetResult.Success)
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "Exception: $it")
                        continuation.resume(PasswordResetResult.InvalidateData)
                    }
            } catch (e: Exception) {
                Log.d(TAG, "catch Exception: $e")
                continuation.resume(PasswordResetResult.Error)
            }
        }
    }
}