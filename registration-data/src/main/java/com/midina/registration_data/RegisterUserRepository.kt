package com.midina.registration_data

import android.util.Log
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserEntity
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.model.User
import kotlinx.coroutines.yield
import java.sql.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TAG = "RegisterUserRepository"

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


    suspend fun writeToRoomDatabase(user: User): ResultEvent<String> {
        return try {
            addUser(user.toUserEntity())
            ResultEvent.Success
        } catch (e: Exception) {
            ResultEvent.Error(e.toString())
        }
    }

    suspend fun writeToFirebaseDataStore(user: User): ResultEvent<String> {
        return suspendCoroutine { continuation ->
            try {
                val db = Firebase.firestore
                val dbUser = hashMapOf(
                    "firstName" to user.firstName,
                    "lastName" to user.lastName,
                    "email" to user.emailAddress,
                    "gender" to user.gender.toString(),
                    "bDay" to user.dateOfBirth
                )

                db.collection("users")
                    .add(dbUser)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        continuation.resume(ResultEvent.Success)
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        continuation.resume(ResultEvent.Error(e.toString()))
                    }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error(e.toString()))
            }
        }
    }

    suspend fun generateMessagingToken(): ResultEvent<String> {
        return suspendCoroutine { continuation ->
            try {
                FirebaseMessaging.getInstance().token
                    .addOnSuccessListener { token ->
                        Log.d(TAG, "Generated token: ${token}")
                        continuation.resume(ResultEvent.Success)
                    }
                    .addOnFailureListener {
                        continuation.resume(ResultEvent.Error(it.toString()))
                    }
            } catch (e: Exception) {
                continuation.resume(ResultEvent.Error(e.toString()))
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
    }
}