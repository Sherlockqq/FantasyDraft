package com.midina.android.login_data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Singleton

@Singleton
class SigningInRepository {

    fun getIsSigned(email: String, password: String) : Boolean {
        val fAuth = Firebase.auth
        var isSign : Boolean = false
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            isSign = task.isSuccessful
        }
        return isSign
    }
}