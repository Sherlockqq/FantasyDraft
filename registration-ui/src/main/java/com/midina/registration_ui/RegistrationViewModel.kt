package com.midina.registration_ui

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.core_ui.ui.State
import com.midina.registration_domain.model.Gender
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegisterUserUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

class RegistrationViewModel @Inject constructor(private val registerUserUsecase: RegisterUserUsecase) :
    ViewModel() {

    private val _registerEvents = MutableStateFlow<RegistrationEvent>(RegistrationEvent.NotRegistered)
    val registerEvents: StateFlow<RegistrationEvent>
        get() = _registerEvents.asStateFlow()

    private val _firstNameEvents = MutableStateFlow<FirstNameUiEvent>(FirstNameUiEvent.OnTextEmpty)
    val firstNameEvents: StateFlow<FirstNameUiEvent>
        get() = _firstNameEvents.asStateFlow()

    private val _lastNameEvents = MutableStateFlow<LastNameUiEvent>(LastNameUiEvent.OnTextEmpty)
    val lastNameEvents: StateFlow<LastNameUiEvent>
        get() = _lastNameEvents.asStateFlow()

    private val _emailEvents = MutableStateFlow<EmailUiEvent>(EmailUiEvent.OnTextEmpty)
    val emailEvents: StateFlow<EmailUiEvent>
        get() = _emailEvents.asStateFlow()

    private val _passwordEvents = MutableStateFlow<PasswordUiEvent>(PasswordUiEvent.OnTextEmpty)
    val passwordEvents: StateFlow<PasswordUiEvent>
        get() = _passwordEvents.asStateFlow()

    private val _daysEvents = MutableStateFlow<DaysUiEvent>(DaysUiEvent.OnTextEmpty)
    val daysEvents: StateFlow<DaysUiEvent>
        get() = _daysEvents.asStateFlow()

    private val _monthesEvents = MutableStateFlow<MonthesUiEvent>(MonthesUiEvent.OnTextEmpty)
    val monthesEvents: StateFlow<MonthesUiEvent>
        get() = _monthesEvents.asStateFlow()

    private val _yearsEvents = MutableStateFlow<YearsUiEvent>(YearsUiEvent.OnTextEmpty)
    val yearsEvents: StateFlow<YearsUiEvent>
        get() = _yearsEvents.asStateFlow()

    private val _firstName = MutableStateFlow("")
    private var firstNameState = State.DEFAULT

      
    private val _lastName = MutableStateFlow("")
   
      private val _email = MutableStateFlow("")


    private var lastNameState = State.DEFAULT
    private val _password = MutableStateFlow("")

    private var gender: Gender = Gender.UNSPECIFIED

    private var emailState = State.DEFAULT
    private val _dateDays = MutableStateFlow(0)

    private val _dateMonthes = MutableStateFlow(0)

    private var passwordState = State.DEFAULT
    private val _dateYears = MutableStateFlow(0)

    private var firstNameState = State.DEFAULT

    private var lastNameState = State.DEFAULT

    private var emailState = State.DEFAULT

    private var passwordState = State.DEFAULT

    private var genderState: State = State.DEFAULT

    var gender: Gender = Gender.UNSPECIFIED
    var dateState: State = State.DEFAULT

    init {

    }

    val firstNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _firstName.value.isEmpty()) {
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        } else {
            if (!hasFocus && _firstName.value.length < 2) {
                firstNameState = State.ERROR
                _firstNameEvents.value = FirstNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onFirstNameChanged(text: Editable?) {
        _firstName.value = text.toString()
        if (text.toString().isEmpty()) {
            firstNameState = State.ERROR
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        } else {
            if (text.toString().length == 2) {
                firstNameState = State.CORRECT
                _firstNameEvents.value = FirstNameUiEvent.OnTextValid
            }
        }
    }

    val lastNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _lastName.value.isEmpty()) {
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        } else {
            if (!hasFocus && _lastName.value.length < 2) {
                lastNameState = State.ERROR
                _lastNameEvents.value = LastNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onLastNameChanged(text: Editable?) {
        _lastName.value = text.toString()
        if (text.toString().isEmpty()) {
            lastNameState = State.ERROR
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        } else {
            if (text.toString().length == 2) {
                lastNameState = State.CORRECT
                _lastNameEvents.value = LastNameUiEvent.OnTextValid
            }
        }
    }

    val emailOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && (_email.value.isEmpty() || _email.value.let { !isEmail(it) })) {
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextInvalid
        }
    }

    fun onEmailChanged(text: Editable?) {
        _email.value = text.toString()
        if (text.toString().isEmpty()) {
            emailState = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextEmpty
        }
        if (isEmail(text.toString())) {
            emailState = State.CORRECT
            _emailEvents.value = EmailUiEvent.OnTextValid
        }
    }

    val passwordOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        val passStr = _password.value
        val size = passStr.length
        if (!hasFocus && size < MIN_CHARS_IN_PASS) {
            passwordState = State.ERROR
            _passwordEvents.value = PasswordUiEvent.OnTextInvalid
        }
    }

    fun onPasswordChanged(text: Editable?) {
        _password.value = text.toString()
        if (text.toString().isEmpty()) {
            passwordState = State.ERROR
            _passwordEvents.value = PasswordUiEvent.OnTextEmpty
        } else {
            val textSize = text.toString().length
            if (textSize == MIN_CHARS_IN_PASS) {
                passwordState = State.CORRECT
                _passwordEvents.value = PasswordUiEvent.OnTextValid
            }
            if (textSize < MIN_CHARS_IN_PASS) {
                passwordState = State.DEFAULT
                _passwordEvents.value = PasswordUiEvent.OnProcess(textSize)
            }
        }
    }

    val daysOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _dateDays.value.toString().isDigitsOnly()) {
            _daysEvents.value = DaysUiEvent.OnNotFocus
        }
    }

    val daysOnTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            daysHandler(text.toString())
        }
    }

    private fun daysHandler(text: String?) {

        if (text?.isEmpty() == true) {
            _daysEvents.value = DaysUiEvent.OnTextEmpty
        } else {
            try {
                val daysNum = text?.toInt()
                _dateDays.value = daysNum
                if (text?.length == DAYS_INT_SIZE) {
                    isDate()
                    _daysEvents.value = DaysUiEvent.OnTextValid
                }
            } catch (e: NumberFormatException) {
                _daysEvents.value = DaysUiEvent.OnTextInvalid
            }
        }
    }

    val monthesOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _dateMonthes.value.toString().isDigitsOnly()) {
            _monthesEvents.value = MonthesUiEvent.OnNotFocus
        }
    }

    val monthesOnTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            monthesHandler(text.toString())
        }
    }


    private fun monthesHandler(text: String?) {

        if (text?.isEmpty() == true) {
            _monthesEvents.value = MonthesUiEvent.OnTextEmpty
        } else {
            try {
                val monthesNum = text?.toInt()
                _dateMonthes.value = monthesNum
                if (text?.length == MONTHES_INT_SIZE) {
                    isDate()
                    _monthesEvents.value = MonthesUiEvent.OnTextValid
                }
            } catch (e: NumberFormatException) {
                _monthesEvents.value = MonthesUiEvent.OnTextInvalid
            }
        }
    }

    val yearsOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _dateYears.value.toString().isDigitsOnly()) {
            _yearsEvents.value = YearsUiEvent.OnNotFocus
        }
    }

    val yearsOnTextChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(text: Editable?) {
            yearsHandler(text.toString())
        }
    }

    private fun yearsHandler(text: String?) {

        if (text?.isEmpty() == true) {
            _yearsEvents.value = YearsUiEvent.OnTextEmpty
        } else {
            try {
                val yearsNum = text?.toInt()
                _dateYears.value = yearsNum
                if (text?.length == YEARS_INT_SIZE) {
                    isDate()
                    _yearsEvents.value = YearsUiEvent.OnTextValid
                }
            } catch (e: NumberFormatException) {
                _yearsEvents.value = YearsUiEvent.OnTextInvalid
            }
        }
    }

    private fun isEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkingAllIsCorrect(): Boolean {
        return firstNameState == State.CORRECT &&
                lastNameState == State.CORRECT &&
                emailState == State.CORRECT &&
                passwordState == State.CORRECT &&
                genderState == State.CORRECT &&
                dateState == State.CORRECT
    }

    private fun isDate() {
        try {
            dateState = if (_dateYears.value in 1901..2020) { //range of valid year
                if (_dateMonthes.value in JANUARY..DECEMBER) {
                    if (checkDaysInMonth()) {
                        State.CORRECT
                    } else {
                        State.ERROR
                    }
                } else {
                    State.ERROR
                }
            } else {
                State.ERROR
            }
        } catch (e: NullPointerException) {
            State.ERROR
        }
    }

    private fun checkDaysInMonth(): Boolean {
        when (_dateMonthes.value) {
            JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> {
                return _dateDays.value in 1..31 //possible days in these monthes
            }
            APRIL, JUNE, SEPTEMBER, NOVEMBER -> {
                return _dateDays.value in 1..30 //possible days in these monthes
            }
            FEBRUARY -> {
                return if (_dateYears.value % 4 == 0) {
                    _dateDays.value in 1..29 //possible days in this month if it is leap year
                } else {
                    _dateDays.value in 1..28 //possible days in this month if it is not leap year
                }
            }
        }
        return false
    }

    private suspend fun registerUser() {
        if (checkingAllIsCorrect()) {
            val date = _dateYears.value.toString() + "-" +
                    _dateMonthes.value.toString() + "-" + _dateDays.value.toString()
            val user = User(
                _firstName.value,
                _lastName.value,
                _email.value,
                gender,
                date
            )
            val result = registerUserUsecase.execute(user, _password.value)
            when (result) {
                ResultEvent.Success -> _registerEvents.value = RegistrationEvent.OnRegistered
                ResultEvent.InvalidData -> {
                    Log.d("RegistrationVM", "Invalid Data")
                    //TODO Exception
                }
                ResultEvent.Error -> {
                    Log.d("RegistrationVM", "Error")
                    //TODO Exception
                }
            }
        } else {
            Log.d("FDSF","FSDFDS")
        }
    }

    fun registrationIsClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            registerUser()
        }
    }

    fun maleClicked() {
        genderState = State.CORRECT
        gender = Gender.MALE
        genderState = State.CORRECT
    }

    fun femaleClicked() {
        genderState = State.CORRECT
        gender = Gender.FEMALE
        genderState = State.CORRECT
    }

    fun unspecifiedClicked() {
        genderState = State.CORRECT
        gender = Gender.UNSPECIFIED
        genderState = State.CORRECT
    }

}

sealed class FirstNameUiEvent {
    object OnTextEmpty : FirstNameUiEvent()
    object OnTextValid : FirstNameUiEvent()
    object OnTextInvalid : FirstNameUiEvent()
}

sealed class LastNameUiEvent {
    object OnTextEmpty : LastNameUiEvent()
    object OnTextValid : LastNameUiEvent()
    object OnTextInvalid : LastNameUiEvent()
}

sealed class EmailUiEvent {
    object OnTextEmpty : EmailUiEvent()
    object OnTextValid : EmailUiEvent()
    object OnTextInvalid : EmailUiEvent()
}

sealed class PasswordUiEvent {
    class OnProcess(val textSize: Int) : PasswordUiEvent()
    object OnTextEmpty : PasswordUiEvent()
    object OnTextValid : PasswordUiEvent()
    object OnTextInvalid : PasswordUiEvent()
}

sealed class DaysUiEvent {
    object OnTextEmpty : DaysUiEvent()
    object OnTextInvalid : DaysUiEvent()
    object OnTextValid : DaysUiEvent()
    object OnNotFocus : DaysUiEvent()
}

sealed class MonthesUiEvent {
    object OnTextEmpty : MonthesUiEvent()
    object OnTextInvalid : MonthesUiEvent()
    object OnTextValid : MonthesUiEvent()
    object OnNotFocus : MonthesUiEvent()
}

sealed class YearsUiEvent {
    object OnTextEmpty : YearsUiEvent()
    object OnTextInvalid : YearsUiEvent()
    object OnTextValid : YearsUiEvent()
    object OnNotFocus : YearsUiEvent()
}

sealed class RegistrationEvent {
    object OnRegistered : RegistrationEvent()
    object NotRegistered : RegistrationEvent()
}