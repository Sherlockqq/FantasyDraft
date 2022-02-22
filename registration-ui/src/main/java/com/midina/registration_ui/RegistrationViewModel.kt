package com.midina.registration_ui

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.registration_domain.model.Gender
import com.midina.registration_domain.model.User
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
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
private const val MIN_YEAR = 1901
private const val MAX_YEAR = 2022

private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)

class RegistrationViewModel @Inject constructor(
    private val registerUserUsecase: RegisterUserUsecase,
    private val writeToDatabaseUsecase: WriteToDatabaseUsecase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _registerEvents =
        MutableStateFlow<RegistrationEvent>(RegistrationEvent.OnDefault)
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
    val firstName: StateFlow<String>
        get() = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String>
        get() = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String>
        get() = _password.asStateFlow()

    private val _dateDays = MutableStateFlow(0)
    val dateDays: StateFlow<Int>
        get() = _dateDays.asStateFlow()

    private val _dateMonthes = MutableStateFlow(0)
    val dateMonthes: StateFlow<Int>
        get() = _dateMonthes.asStateFlow()

    private val _dateYears = MutableStateFlow(0)
    val dateYears: StateFlow<Int>
        get() = _dateYears.asStateFlow()

    private val _firstNameState = MutableStateFlow(State.DEFAULT)
    val firstNameState: StateFlow<State>
        get() = _firstNameState.asStateFlow()

    private val _lastNameState = MutableStateFlow(State.DEFAULT)
    val lastNameState: StateFlow<State>
        get() = _lastNameState.asStateFlow()

    private val _emailState = MutableStateFlow(State.DEFAULT)
    val emailState: StateFlow<State>
        get() = _emailState.asStateFlow()

    private var _passwordState = MutableStateFlow(State.DEFAULT)
    val passwordState: StateFlow<State>
        get() = _passwordState.asStateFlow()

    private val _genderState = MutableStateFlow(State.DEFAULT)
    val genderState: StateFlow<State>
        get() = _genderState.asStateFlow()

    private val _gender = MutableStateFlow(Gender.UNSPECIFIED)
    val gender: StateFlow<Gender>
        get() = _gender.asStateFlow()

    private val _dateState = MutableStateFlow(State.DEFAULT)
    val dateState: StateFlow<State>
        get() = _dateState.asStateFlow()

    val firstNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _firstName.value.isEmpty()) {
            _firstNameState.value = State.DEFAULT
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        } else {
            if (!hasFocus && _firstName.value.length < 2) {
                _firstNameState.value = State.ERROR
                _firstNameEvents.value = FirstNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onFirstNameChanged(text: Editable?) {
        _firstName.value = text.toString()

        if (firstName.value.length < 2) {
            _firstNameState.value = State.DEFAULT
            _firstNameEvents.value = FirstNameUiEvent.OnTextEmpty
        } else if (firstName.value.length == 2) {
            _firstNameState.value = State.CORRECT
            _firstNameEvents.value = FirstNameUiEvent.OnTextValid
        }
    }

    val lastNameOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _lastName.value.isEmpty()) {
            _lastNameState.value = State.DEFAULT
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        } else {
            if (!hasFocus && _lastName.value.length < 2) {
                _lastNameState.value = State.ERROR
                _lastNameEvents.value = LastNameUiEvent.OnTextInvalid
            }
        }
    }

    fun onLastNameChanged(text: Editable?) {
        _lastName.value = text.toString()

        if (lastName.value.length < 2) {
            _lastNameState.value = State.DEFAULT
            _lastNameEvents.value = LastNameUiEvent.OnTextEmpty
        } else if (lastName.value.length == 2) {
            _lastNameState.value = State.CORRECT
            _lastNameEvents.value = LastNameUiEvent.OnTextValid
        }
    }

    val emailOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _email.value.let { !isEmail(it) }) {
            _emailState.value = State.ERROR
            _emailEvents.value = EmailUiEvent.OnTextInvalid
        }
    }

    fun onEmailChanged(text: Editable?) {
        _email.value = text.toString()
        if (isEmail(_email.value)) {
            _emailState.value = State.CORRECT
            _emailEvents.value = EmailUiEvent.OnTextValid
        } else {
            _emailState.value = State.DEFAULT
            _emailEvents.value = EmailUiEvent.OnTextEmpty
        }
    }

    val passwordOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && password.value.isNotEmpty()
            && password.value.length < MIN_CHARS_IN_PASS
        ) {
            _passwordState.value = State.ERROR
            _passwordEvents.value = PasswordUiEvent.OnTextInvalid
        }
    }

    fun onPasswordChanged(text: Editable?) {
        _password.value = text.toString()
        if (text.toString().isEmpty()) {
            _passwordState.value = State.DEFAULT
            _passwordEvents.value = PasswordUiEvent.OnTextEmpty
        } else {
            val textSize = text.toString().length
            if (textSize == MIN_CHARS_IN_PASS) {
                _passwordState.value = State.CORRECT
                _passwordEvents.value = PasswordUiEvent.OnTextValid
            }
            if (textSize < MIN_CHARS_IN_PASS) {
                _passwordState.value = State.DEFAULT
                _passwordEvents.value = PasswordUiEvent.OnProcess(textSize)
            }
        }
    }

    val daysOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _dateDays.value.toString()
                .isDigitsOnly() && _dateDays.value.toString().length == 1
        ) {
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

    private fun daysHandler(text: String) {

        if (text.isEmpty()) {
            _daysEvents.value = DaysUiEvent.OnTextEmpty
        } else {
            _dateDays.value = text.toInt()
            if (text.length == DAYS_INT_SIZE) {
                _daysEvents.value = DaysUiEvent.OnFinish
                if (_daysEvents.value != DaysUiEvent.OnTextEmpty
                    && _monthesEvents.value != MonthesUiEvent.OnTextEmpty
                    && _yearsEvents.value != YearsUiEvent.OnTextEmpty
                ) {
                    checkDate()
                }
            }
        }
    }

    val monthesOnFocusListener = View.OnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && _dateMonthes.value.toString()
                .isDigitsOnly() && _dateMonthes.value.toString().length == 1
        ) {
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


    private fun monthesHandler(text: String) {
        if (text.isEmpty()) {
            _monthesEvents.value = MonthesUiEvent.OnTextEmpty
        } else {
            _dateMonthes.value = text.toInt()
            if (text.length == MONTHES_INT_SIZE) {
                _monthesEvents.value = MonthesUiEvent.OnFinish
                if (_daysEvents.value != DaysUiEvent.OnTextEmpty
                    && _monthesEvents.value != MonthesUiEvent.OnTextEmpty
                    && _yearsEvents.value != YearsUiEvent.OnTextEmpty
                ) {
                    checkDate()
                }
            }
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

    private fun yearsHandler(text: String) {
        if (text.isEmpty()) {
            _yearsEvents.value = YearsUiEvent.OnTextEmpty
        } else {
            _dateYears.value = text.toInt()
            if (text.length == YEARS_INT_SIZE) {
                _yearsEvents.value = YearsUiEvent.OnFinish
                if (_daysEvents.value != DaysUiEvent.OnTextEmpty
                    && _monthesEvents.value != MonthesUiEvent.OnTextEmpty
                    && _yearsEvents.value != YearsUiEvent.OnTextEmpty
                ) {
                    checkDate()
                }
            }
        }
    }

    private fun isEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    private fun checkingAllIsCorrect(): Boolean {
        return firstNameState.value == State.CORRECT &&
                lastNameState.value == State.CORRECT &&
                emailState.value == State.CORRECT &&
                passwordState.value == State.CORRECT &&
                genderState.value == State.CORRECT &&
                dateState.value == State.CORRECT
    }

    private fun checkDate() {
        if (_dateYears.value in MIN_YEAR..MAX_YEAR) { //range of valid year
            if (_dateMonthes.value in JANUARY..DECEMBER) {
                if (checkDaysInMonth()) {
                    _dateState.value = State.CORRECT
                } else {
                    _dateState.value = State.ERROR
                }
            } else {
                _dateState.value = State.ERROR
            }
        } else {
            _dateState.value = State.ERROR
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
        _registerEvents.value = RegistrationEvent.OnProgress
        if (checkingAllIsCorrect()) {
            val date = _dateYears.value.toString() + "-" +
                    _dateMonthes.value.toString() + "-" + _dateDays.value.toString()
            val user = User(
                _firstName.value,
                _lastName.value,
                _email.value,
                gender.value,
                date
            )
            val result = registerUserUsecase.execute(user, _password.value)
            when (result) {
                ResultEvent.Success -> {
                    writeToDatabase(user)
                    _registerEvents.value = RegistrationEvent.OnSuccess
                }
                ResultEvent.Error -> {
                    _registerEvents.value = RegistrationEvent.OnError
                }
                ResultEvent.InProgress -> {
                    _registerEvents.value = RegistrationEvent.OnProgress
                }
            }
        } else {
            setErrors()
            _registerEvents.value = RegistrationEvent.OnError
        }
    }

    private suspend fun writeToDatabase(user: User) {
        val result = writeToDatabaseUsecase.execute(user)

        when (result) {
            ResultEvent.Success -> {
                Log.d(TAG, "Success")
            }
            ResultEvent.Error -> {
                Log.d(TAG, "Error")
            }
            ResultEvent.InProgress -> {
                Log.d(TAG, "InProgress")
            }
        }

    }

    private fun setErrors() {
        if (firstNameState.value == State.DEFAULT) {
            _firstNameState.value = State.ERROR
        }
        if (lastNameState.value == State.DEFAULT) {
            _lastNameState.value = State.ERROR
        }
        if (emailState.value == State.DEFAULT) {
            _emailState.value = State.ERROR
        }
        if (passwordState.value == State.DEFAULT) {
            _passwordState.value = State.ERROR
        }
        if (genderState.value == State.DEFAULT) {
            _genderState.value = State.ERROR
        }
        if (dateState.value == State.DEFAULT) {
            _dateState.value = State.ERROR
        }
    }

    fun registrationIsClicked() {
        viewModelScope.launch(dispatcher) {
            registerUser()
        }
    }

    fun maleClicked() {
        _gender.value = Gender.MALE
        _genderState.value = State.CORRECT
    }

    fun femaleClicked() {
        _gender.value = Gender.FEMALE
        _genderState.value = State.CORRECT
    }

    fun unspecifiedClicked() {
        _gender.value = Gender.UNSPECIFIED
        _genderState.value = State.CORRECT
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
    object OnFinish : DaysUiEvent()
    object OnNotFocus : DaysUiEvent()
}

sealed class MonthesUiEvent {
    object OnTextEmpty : MonthesUiEvent()
    object OnFinish : MonthesUiEvent()
    object OnNotFocus : MonthesUiEvent()
}

sealed class YearsUiEvent {
    object OnTextEmpty : YearsUiEvent()
    object OnFinish : YearsUiEvent()
}

sealed class RegistrationEvent {
    object OnSuccess : RegistrationEvent()
    object OnError : RegistrationEvent()
    object OnProgress : RegistrationEvent()
    object OnDefault : RegistrationEvent()
}

sealed class WritingToDatabaseEvent {
    object OnSuccess : WritingToDatabaseEvent()
    object OnError : WritingToDatabaseEvent()
}

enum class State {
    DEFAULT,
    ERROR,
    CORRECT
}