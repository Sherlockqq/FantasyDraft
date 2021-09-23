package com.midina.android.registration_data.usecaseimpl

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_domain.usecase.registrUser

class registrUserImpl() : registrUser {
    override suspend fun execute(email: String, password: String): Boolean {

        val fAuth = Firebase.auth
        var isCreate : Boolean = false

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            isCreate = task.isSuccessful
        }
        return isCreate
    }
}