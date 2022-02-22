package com.midina.registration_data

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserEntity
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
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
                        if (task.isComplete) {
                            if (task.isSuccessful) {
                                fAuth.currentUser?.sendEmailVerification()
                                    ?.addOnCompleteListener { verifyTask ->
                                    if (verifyTask.isComplete) {
                                        if (verifyTask.isSuccessful) {
                                            continuation.resume(ResultEvent.Success)
                                        } else {
                                            continuation.resume(ResultEvent.Error)
                                        }
                                    } else {
                                        continuation.resume(ResultEvent.InProgress)
                                    }
                                }
                            } else {
                                continuation.resume(ResultEvent.Error)
                            }
                        } else {
                            continuation.resume(ResultEvent.InProgress)
                        }

                    }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error)
            }
        }
    }

    suspend fun writeToDatabase(user: User): ResultEvent {
        return try {
            addUser(user.toUserEntity())
            ResultEvent.Success
        } catch (e: Exception) {
            ResultEvent.Error
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
    }
}