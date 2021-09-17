package com.example.fantasydraft.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegistrationViewModel: ViewModel() {

    private val _firstName = MutableLiveData<String>()
    val firstName : LiveData<String>
        get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName : LiveData<String>
        get() = _lastName

    private val _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    private val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password

    private val _dateDays = MutableLiveData<Int>()
    val dateDays : LiveData<Int>
        get() = _dateDays

    private val _dateMonthes = MutableLiveData<Int>()
    val dateMonthes : LiveData<Int>
        get() = _dateMonthes

    private val _dateYear = MutableLiveData<Int>()
    val dateYear : LiveData<Int>
        get() = _dateYear

    init {

    }

    private fun checkingEmail(email: String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isEmail(email : String):Boolean{
        return checkingEmail(email)
    }

}