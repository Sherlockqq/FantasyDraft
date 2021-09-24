package com.midina.registration_data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Singleton


@Singleton
class RegistrUserRepository {

    fun getIsRegistered(email: String, password: String) : Boolean {
        val fAuth = Firebase.auth
        var isCreate : Boolean = false

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            isCreate = task.isSuccessful
        }
        return isCreate
    }
}