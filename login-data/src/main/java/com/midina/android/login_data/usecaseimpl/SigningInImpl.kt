package com.midina.android.login_data.usecaseimpl

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.login_domain.usecase.SigningIn

class SigningInImpl(): SigningIn {
    override suspend fun execute(email: String, password: String): Boolean{
        val fAuth = Firebase.auth
        var isSign : Boolean = false
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            isSign = task.isSuccessful
        }
        return isSign
    }
}

