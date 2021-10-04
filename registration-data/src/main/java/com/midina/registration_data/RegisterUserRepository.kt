package com.midina.registration_data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_data.database.UserDao
import com.midina.registration_data.database.UserEntity
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class RegisterUserRepository @Inject constructor(
    private val userDao: UserDao) {

   suspend fun getIsRegistered(user: User, password: String) : ResultEvent {
       return suspendCoroutine { continuation ->
           try{
               val fAuth = Firebase.auth
               fAuth.createUserWithEmailAndPassword(user.emailAddress, password).addOnCompleteListener {
                       task ->
                   if (task.isSuccessful) {
                       continuation.resume(ResultEvent.Success)
                       val userEntity = UserEntity(
                           user.emailAddress,
                           user.firstName,
                           user.lastName,
                           user.gender)
                       GlobalScope.launch{
                           addUser(userEntity)
                       }
                   } else {
                       continuation.resume(ResultEvent.InvalidData)
                   }
               }
           }catch (e:Exception){
               continuation.resume(ResultEvent.Error)
           }
       }
   }
    suspend fun addUser(user:UserEntity){
        userDao.insert(user)
    }
}