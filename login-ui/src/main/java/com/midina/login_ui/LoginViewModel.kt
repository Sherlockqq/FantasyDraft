package com.midina.login_ui

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel: ViewModel() {

    private val fAuth = Firebase.auth

    private val _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password

    init{
        _email.value = ""
        _password.value = ""
    }

    val emailOnTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            _email.value = text.toString()
        }
    }



    val passwordOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(text: Editable?) {
            _password.value = text.toString()
        }
    }

    private fun signingIn(){
        fAuth.signInWithEmailAndPassword(_email.value.toString(),
            _password.value.toString()).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Log.i("SIGN IN","SIGNED IN")
                }else{
                    Log.i("SIGN IN","NOT SIGNED IN")
                }
        }
    }

    fun signInClicked(){
        signingIn()
    }
}