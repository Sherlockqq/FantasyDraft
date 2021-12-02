package com.midina.registration_data

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserEntity
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import kotlinx.coroutines.*
import java.io.EOFException
import java.sql.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class RegisterUserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun getIsRegistered(user: User, password: String): ResultEvent {
        return suspendCoroutine { continuation ->
            try {
                val fAuth = Firebase.auth
                fAuth.createUserWithEmailAndPassword(user.emailAddress, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            continuation.resume(ResultEvent.Success)

                            GlobalScope.launch {
                                try{
                                    addUser(user.toUserEntity())
                                } catch (e: EOFException) {
                                    Log.d("RegisterRepository", "${e.message}")
                                }
                            }
                        } else {
                            continuation.resume(ResultEvent.InvalidData)
                        }
                    }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error)
            }
        }
    }

    private fun User.toUserEntity() = UserEntity(
        this.emailAddress,
        this.firstName,
        this.lastName,
        this.gender,
        Date.valueOf(this.dateOfBirth)
    )

    private suspend fun addUser(user: UserEntity) {
        userDao.insert(user)
        Log.d("DATABASE", "User Added")
    }
}