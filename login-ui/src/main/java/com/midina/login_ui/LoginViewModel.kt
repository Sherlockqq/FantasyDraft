package com.midina.login_ui

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.login_domain.usecase.SigningIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginViewModel @Inject constructor(private val signingIn : SigningIn): ViewModel() {


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

    fun signInClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            signIn()
        }
    }
    suspend fun signIn(){
        signingIn.execute(_email.value.toString(),_password.value.toString())
    }
}