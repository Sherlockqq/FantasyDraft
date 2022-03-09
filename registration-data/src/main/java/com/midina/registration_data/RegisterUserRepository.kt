package com.midina.registration_data

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserEntity
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.model.User
import java.sql.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class RegisterUserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun getIsRegistered(email: String, password: String): ResultEvent<String> {

        val action = ActionCodeSettings.newBuilder()
            .setUrl("https://www.example.com/?curPage=1")
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
            .setAndroidPackageName(
                "com.example.fantasydraft",
                true,  /* installIfNotAvailable */
                "1.0" /* minimumVersion */
            )
            .build()

        return suspendCoroutine { continuation ->
            try {
                val fAuth = Firebase.auth
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isComplete) {
                            if (task.isSuccessful) {
                                fAuth.currentUser?.sendEmailVerification(action)
                                    ?.addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isComplete) {
                                            if (verifyTask.isSuccessful) {
                                                continuation.resume(ResultEvent.Success)
                                            } else {
                                                val error = verifyTask.exception?.message
                                                continuation.resume(ResultEvent.Error(error))
                                            }
                                        } else {
                                            continuation.resume(ResultEvent.InProgress)
                                        }
                                    }
                            } else {
                                val error = task.exception?.message
                                continuation.resume(ResultEvent.Error(error))
                            }
                        } else {
                            continuation.resume(ResultEvent.InProgress)
                        }

                    }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error(e.toString()))
            }
        }
    }

    suspend fun writeToDatabase(user: User): ResultEvent<String> {
        return try {
            addUser(user.toUserEntity())
            ResultEvent.Success
        } catch (e: Exception) {
            ResultEvent.Error(e.toString())
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