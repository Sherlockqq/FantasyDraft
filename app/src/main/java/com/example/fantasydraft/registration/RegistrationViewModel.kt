package com.example.fantasydraft.registration

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fantasydraft.utils.State
import com.midina.core_ui.ui.SingleLiveEvent
import java.lang.NumberFormatException

class RegistrationViewModel: ViewModel() {



    private val _firstNameEvents = SingleLiveEvent<FirstNameUiEvent>()
    val firstNameEvents: LiveData<FirstNameUiEvent>
        get() = _firstNameEvents

    private val _lastNameEvents = SingleLiveEvent<LastNameUiEvent>()
    val lastNameEvents: LiveData<LastNameUiEvent>
        get() = _lastNameEvents

    private val _emailEvents = SingleLiveEvent<EmailUiEvent>()
    val emailEvents: LiveData<EmailUiEvent>
        get() = _emailEvents

    private val _passwordEvents = SingleLiveEvent<PasswordUiEvent>()
    val passwordEvents: LiveData<PasswordUiEvent>
        get() = _passwordEvents

    private val _daysEvents = SingleLiveEvent<DaysUiEvent>()
    val daysEvents: LiveData<DaysUiEvent>
        get() = _daysEvents

    private val _monthesEvents = SingleLiveEvent<MonthesUiEvent>()
    val monthesEvents: LiveData<MonthesUiEvent>
        get() = _monthesEvents

    private val _firstName = MutableLiveData<String>()
    val firstName : LiveData<String>
        get() = _firstName

    var firstNameState = State.DEFAULT


    val firstNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _firstName.value?.isEmpty() == true) {
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        }
    }

    val firstNameOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            _firstName.value = text.toString()
            firstNameHandler(text)
        }

    }

    val lastNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _lastName.value?.isEmpty() == true) {
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        }
    }

    val lastNameOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            _lastName.value = text.toString()
            lastNameHandler(text)
        }
    }

    val emailOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && !_email.value?.let { isEmail(it) }!!) {
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextInvalid

        }
    }

    val emailOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            _email.value = text.toString()
            emailHandler(text)
        }
    }

    val passwordOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        val passStr = _password.value
        if (passStr != null) {
            val size = passStr.length
            if(!hasFocus && size < 8) {
                passwordState = State.ERROR
                _passwordEvents.value = PasswordUiEvent.OnTextInvalid
            }
        }
    }

    val passwordOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(text: Editable?) {
            _password.value = text.toString()
            passwordHandler(text)
        }
    }

    val daysOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _dateDays.value.toString().isDigitsOnly()) {
            daysState = State.CORRECT
            _daysEvents.value = DaysUiEvent.OnNotFocus
        }
    }

    val daysOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(text: Editable?) {
            daysHandler(text)
        }
    }

    val monthesOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _dateMonthes.value.toString().isDigitsOnly()) {
            monthesState = State.CORRECT
            _monthesEvents.value = MonthesUiEvent.OnNotFocus
        }
    }

    val monthesOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(text: Editable?) {
            monthesHandler(text)
        }
    }

    private fun firstNameHandler(text: Editable?){
        if(text.toString().isEmpty()){
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        }else{
            firstNameState = State.CORRECT
            _firstNameEvents.value = FirstNameUiEvent.OnTextValid
        }
    }

    private fun lastNameHandler(text: Editable?) {
        if(text.toString().isEmpty()){
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        }else{
            lastNameState = State.CORRECT
            _lastNameEvents.value = LastNameUiEvent.OnTextValid
        }
    }

    private fun emailHandler(text:Editable?){
        if(text.toString().isEmpty()){
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextEmpty
        }
        if(isEmail(text.toString())){
            emailState = State.CORRECT
            _emailEvents.value = EmailUiEvent.OnTextValid
        }
    }

    private fun passwordHandler(text:Editable?){
        if(text.toString().isEmpty()) {
            passwordState = State.ERROR
            _passwordEvents.value = PasswordUiEvent.OnTextEmpty
        }
        else {
            val textSize = text.toString().length
            if(textSize == 8){
                passwordState = State.CORRECT
                _passwordEvents.value = PasswordUiEvent.OnTextValid
            }
            if(textSize <= 8){
                _passwordEvents.value = PasswordUiEvent.OnProcess(textSize)
            }
        }
    }

    private fun daysHandler(text:Editable?){

        if(text.toString().isEmpty()){
            daysState = State.ERROR
            _daysEvents.value = DaysUiEvent.OnTextEmpty
        }else{
            try{
                val daysNum: Int = text.toString().toInt()

                if(daysNum>=0){
                    _dateDays.value = daysNum
                    _daysEvents.value = DaysUiEvent.OnTextValid
                    daysState = State.CORRECT
                }else{
                    daysState = State.ERROR
                    _daysEvents.value = DaysUiEvent.OnTextInvalid
                }
            }catch (e: NumberFormatException){
                daysState = State.ERROR
                _daysEvents.value = DaysUiEvent.OnTextInvalid
            }
        }
    }

    private fun monthesHandler(text:Editable?){

        if(text.toString().isEmpty()){
            monthesState = State.ERROR
            _monthesEvents.value = MonthesUiEvent.OnTextEmpty
        }else{
            try{
                val monthesNum: Int = text.toString().toInt()

                if(monthesNum>=0){
                    _dateMonthes.value = monthesNum
                    _monthesEvents.value = MonthesUiEvent.OnTextValid
                    monthesState = State.CORRECT
                }else{
                    monthesState = State.ERROR
                    _monthesEvents.value = MonthesUiEvent.OnTextInvalid
                }
            }catch (e: NumberFormatException){
                monthesState = State.ERROR
                _monthesEvents.value = MonthesUiEvent.OnTextInvalid
            }
        }
    }

    private val _lastName = MutableLiveData<String>()
    val lastName : LiveData<String>
        get() = _lastName

    var lastNameState = State.DEFAULT
    private set

    private val _email = MutableLiveData<String>()
    val email : LiveData<String>
        get() = _email

    var emailState = State.DEFAULT

    private val _password = MutableLiveData<String>()
    val password : LiveData<String>
        get() = _password

    var passwordState = State.DEFAULT

    private val _dateDays = MutableLiveData<Int>()
    val dateDays : LiveData<Int>
        get() = _dateDays

    var daysState : State = State.DEFAULT

    private val _dateMonthes = MutableLiveData<Int>()
    val dateMonthes : LiveData<Int>
        get() = _dateMonthes

    var monthesState : State = State.DEFAULT

    private val _dateYear = MutableLiveData<Int>()
    val dateYear : LiveData<Int>
        get() = _dateYear

    var yearState : State = State.DEFAULT

    var genderState : State = State.DEFAULT

    init{
        _firstName.value = ""
        _lastName.value = ""
    }

    private fun isEmail(email: String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun checkingAllIsCorrect():Boolean{
        return firstNameState==State.CORRECT &&
                lastNameState==State.CORRECT &&
                emailState==State.CORRECT &&
                passwordState==State.CORRECT &&
                daysState==State.CORRECT &&
                monthesState==State.CORRECT &&
                yearState==State.CORRECT
    }
}

sealed class FirstNameUiEvent{
    object OnTextEmpty : FirstNameUiEvent()
    object OnTextValid : FirstNameUiEvent()
    //TODO Validation
}

sealed class LastNameUiEvent {
    object OnTextEmpty : LastNameUiEvent()
    object OnTextValid : LastNameUiEvent()
  //  object OnTextInvalid: LastNameUiEvent()
    //TODO Validation
}

sealed class EmailUiEvent{
    object OnTextEmpty : EmailUiEvent()
    object OnTextValid : EmailUiEvent()
    object OnTextInvalid : EmailUiEvent()
}

sealed class PasswordUiEvent{
    class OnProcess(val textSize : Int) : PasswordUiEvent()
    object OnTextEmpty : PasswordUiEvent()
    object OnTextValid : PasswordUiEvent()
    object OnTextInvalid : PasswordUiEvent()
}

sealed class DaysUiEvent{
    object OnTextEmpty : DaysUiEvent()
    object OnTextInvalid : DaysUiEvent()
    object OnTextValid : DaysUiEvent()
    object OnNotFocus : DaysUiEvent()
}

sealed class MonthesUiEvent{
    object OnTextEmpty : MonthesUiEvent()
    object OnTextInvalid : MonthesUiEvent()
    object OnTextValid : MonthesUiEvent()
    object OnNotFocus : MonthesUiEvent()
}