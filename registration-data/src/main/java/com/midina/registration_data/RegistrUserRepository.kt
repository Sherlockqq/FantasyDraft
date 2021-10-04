package com.midina.registration_data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.registration_domain.model.ResultEvent
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class RegistrUserRepository {

   suspend fun getIsRegistered(email: String, password: String) : ResultEvent {
       return suspendCoroutine { continuation ->
           try{
               val fAuth = Firebase.auth
               fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       continuation.resume(ResultEvent.Success)
                   } else {
                       continuation.resume(ResultEvent.InvalidData)
                   }
               }
           }catch (e:Exception){
               continuation.resume(ResultEvent.Error)
           }
       }
   }
}