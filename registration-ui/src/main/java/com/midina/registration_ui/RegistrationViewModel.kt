package com.midina.registration_ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.midina.core_ui.ui.State
import com.midina.core_ui.ui.SingleLiveEvent
import com.midina.registration_domain.usecase.RegistrUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

private const val JANUARY = 1
private const val FEBRUARY = 2
private const val MARCH = 3
private const val APRIL = 4
private const val MAY = 5
private const val JUNE = 6
private const val JULY = 7
private const val AUGUST = 8
private const val SEPTEMBER = 9
private const val OCTOBER = 10
private const val NOVEMBER = 11
private const val DECEMBER = 12

private const val MIN_CHARS_IN_PASS = 8
private const val DAYS_INT_SIZE = 2
private const val MONTHES_INT_SIZE = 2
private const val YEARS_INT_SIZE = 4

class RegistrationViewModel @Inject constructor(private val RegistrUser: RegistrUser): ViewModel() {

    private val _registEvents = SingleLiveEvent<RegistrationEvent>()
    val registEvents : LiveData<RegistrationEvent>
        get() = _registEvents

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

    private val _yearsEvents = SingleLiveEvent<YearsUiEvent>()
    val yearsEvents: LiveData<YearsUiEvent>
        get() = _yearsEvents

    private val _firstName = MutableLiveData<String>()
    val firstName : LiveData<String>
        get() = _firstName

    var firstNameState = State.DEFAULT

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

    private val _dateMonthes = MutableLiveData<Int>()
    val dateMonthes : LiveData<Int>
        get() = _dateMonthes

    private val _dateYears = MutableLiveData<Int>()
    val dateYears : LiveData<Int>
        get() = _dateYears

    var dateState : State = State.DEFAULT

    var genderState : State = State.DEFAULT

    init{
        _firstName.value = ""
        _lastName.value = ""
        _email.value = ""
        _password.value = ""
    }

    val firstNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _firstName.value?.isEmpty() == true) {
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        }else{
            if(!hasFocus && _firstName.value.toString().length<2) {
                firstNameState = State.ERROR
                _firstNameEvents.value = FirstNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onFirstNameChanged(text: Editable?){
        _firstName.value = text.toString()
        if(text.toString().isEmpty()){
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        }else{
            if(text.toString().length>=2){
                firstNameState = State.CORRECT
                _firstNameEvents.value = FirstNameUiEvent.OnTextValid
            }
        }
    }

    val lastNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _lastName.value?.isEmpty() == true) {
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        }else{
            if(!hasFocus && _lastName.value.toString().length<2) {
                lastNameState = State.ERROR
                _lastNameEvents.value = LastNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onLastNameChanged(text: Editable?) {
        _lastName.value = text.toString()
        if(text.toString().isEmpty()){
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        }else{
            if(text.toString().length>=2){
                lastNameState = State.CORRECT
                _lastNameEvents.value = LastNameUiEvent.OnTextValid
            }
        }
    }

    val emailOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && (_email.value?.isEmpty() == true || _email.value?.let { !isEmail(it) }!!)){
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextInvalid
        }
    }

    fun onEmailChanged(text:Editable?){
        _email.value = text.toString()
        if(text.toString().isEmpty()){
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextEmpty
        }
        if(isEmail(text.toString())){
            emailState = State.CORRECT
            _emailEvents.value = EmailUiEvent.OnTextValid
        }
    }

    val passwordOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        val passStr = _password.value
        val size = passStr?.length
        if (size != null) {
            if(!hasFocus && size < MIN_CHARS_IN_PASS) {
                passwordState = State.ERROR
                _passwordEvents.value = PasswordUiEvent.OnTextInvalid
            }
        }
    }

    fun onPasswordChanged(text:Editable?){
        _password.value = text.toString()
        if(text.toString().isEmpty()) {
            passwordState = State.ERROR
            _passwordEvents.value = PasswordUiEvent.OnTextEmpty
        }
        else {
            val textSize = text.toString().length
            if(textSize == MIN_CHARS_IN_PASS){
                passwordState = State.CORRECT
                _passwordEvents.value = PasswordUiEvent.OnTextValid
            }
            if(textSize < MIN_CHARS_IN_PASS){
                passwordState = State.DEFAULT
                _passwordEvents.value = PasswordUiEvent.OnProcess(textSize)
            }
        }
    }

    val daysOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _dateDays.value.toString().isDigitsOnly()) {
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

    private fun daysHandler(text:Editable?){

        if(text.toString().isEmpty()){
            _daysEvents.value = DaysUiEvent.OnTextEmpty
        }else{
            try{
                val daysNum: Int = text.toString().toInt()
                _dateDays.value = daysNum
                if(text.toString().length == DAYS_INT_SIZE){
                    isDate()
                    _daysEvents.value = DaysUiEvent.OnTextValid
                }
            }catch (e: NumberFormatException){
                _daysEvents.value = DaysUiEvent.OnTextInvalid
            }
        }
    }

    val monthesOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _dateMonthes.value.toString().isDigitsOnly()) {
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

    private fun monthesHandler(text:Editable?){

        if(text.toString().isEmpty()){
            _monthesEvents.value = MonthesUiEvent.OnTextEmpty
        }else{
            try{
                val monthesNum: Int = text.toString().toInt()
                _dateMonthes.value = monthesNum
                if(text.toString().length == MONTHES_INT_SIZE){
                    isDate()
                    _monthesEvents.value = MonthesUiEvent.OnTextValid
                }
            }catch (e: NumberFormatException){
                _monthesEvents.value = MonthesUiEvent.OnTextInvalid
            }
        }
    }

    val yearsOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if(!hasFocus && _dateYears.value.toString().isDigitsOnly()) {
            _yearsEvents.value = YearsUiEvent.OnNotFocus
        }
    }

    val yearsOnTextChangeListener = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun afterTextChanged(text: Editable?) {
            yearsHandler(text)
        }
    }


    private fun yearsHandler(text:Editable?){

        if(text.toString().isEmpty()){
            _yearsEvents.value = YearsUiEvent.OnTextEmpty
        }else{
            try{
                val yearsNum: Int = text.toString().toInt()
                _dateYears.value = yearsNum
                if(text.toString().length == YEARS_INT_SIZE){
                    isDate()
                    _yearsEvents.value = YearsUiEvent.OnTextValid
                }
            }catch (e: NumberFormatException){
                _yearsEvents.value = YearsUiEvent.OnTextInvalid
            }
        }
    }

    private fun isEmail(email: String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkingAllIsCorrect():Boolean{
        return firstNameState== State.CORRECT &&
                lastNameState== State.CORRECT &&
                emailState== State.CORRECT &&
                passwordState== State.CORRECT &&
                genderState== State.CORRECT &&
                dateState== State.CORRECT
    }

    private fun isDate(){
        try{
            if(dateDays.value != null || dateDays.value.toString().isNotEmpty() &&
                dateMonthes.value != null || dateMonthes.value.toString().isNotEmpty() &&
                dateYears.value != null || dateYears.value.toString().isNotEmpty()){
                dateState = if(dateYears.value!! in 1901..2020){ //range of valid year
                    if (dateMonthes.value!! in JANUARY..DECEMBER){
                        if(checkDaysInMonth()){
                            State.CORRECT
                        }else{
                            State.ERROR
                        }
                    }else{
                        State.ERROR
                    }
                }else{
                    State.ERROR
                }
            }
        }catch(e : NullPointerException){
            State.ERROR
        }

    }

    private fun checkDaysInMonth() : Boolean{
        when(dateMonthes.value){
            JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> {
                return dateDays.value in 1..31 //possible days in these monthes
            }
            APRIL, JUNE, SEPTEMBER, NOVEMBER ->{
                return dateDays.value in 1..30 //possible days in these monthes
            }
            FEBRUARY -> {
                return if(dateYears.value!! %4 == 0){
                    dateDays.value in 1..29 //possible days in this month if it is leap year
                }else{
                    dateDays.value in 1..28 //possible days in this month if it is not leap year
                }
            }
        }
        return false
    }

    private suspend fun registrUser(){
        if(checkingAllIsCorrect()){
            RegistrUser.execute(_email.value.toString(),_password.value.toString())
            _registEvents.postValue(RegistrationEvent.OnRegistered)
        }
    }

    fun registrationIsClicked(){
        viewModelScope.launch(Dispatchers.IO) {
            registrUser()
        }
    }

}

sealed class FirstNameUiEvent{
    object OnTextEmpty : FirstNameUiEvent()
    object OnTextValid : FirstNameUiEvent()
    object OnTextInvalid: FirstNameUiEvent()
}

sealed class LastNameUiEvent {
    object OnTextEmpty : LastNameUiEvent()
    object OnTextValid : LastNameUiEvent()
    object OnTextInvalid: LastNameUiEvent()
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

sealed class YearsUiEvent{
    object OnTextEmpty : YearsUiEvent()
    object OnTextInvalid : YearsUiEvent()
    object OnTextValid : YearsUiEvent()
    object OnNotFocus : YearsUiEvent()
}

sealed class RegistrationEvent{
    object OnRegistered :RegistrationEvent()
}