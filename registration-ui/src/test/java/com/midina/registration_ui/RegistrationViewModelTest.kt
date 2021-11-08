package com.midina.registration_ui

import android.text.Editable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.midina.core_ui.ui.State
import com.midina.registration_domain.usecase.RegisterUserUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RegistrationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var usecase: RegisterUserUsecase
    private lateinit var viewModel: RegistrationViewModel

    @Before
    fun setupViewModel() {
        usecase = mock(RegisterUserUsecase::class.java)
        viewModel = RegistrationViewModel(usecase)
    }

    @Test
    fun checkInCorrectDay() {
        val observer = Observer<Int> {}
        try {

            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("0")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("10")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectMonth() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("0")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectYear() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("10")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2021")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectSeptember() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("10")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.CORRECT, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectSeptember() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("31")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("10")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.CORRECT, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectJanuary() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("31")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.CORRECT, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectJanuary() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("32")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectFebruaryLeapYear() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("29")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("2")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2000")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)
            assertEquals(State.CORRECT, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectFebruaryLeapYear() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)


            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("30")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("2")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2000")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectFebruaryNotLeapYear() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("28")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("2")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2001")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.CORRECT, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkInCorrectFebruaryNotLeapYear() {
        val observer = Observer<Int> {}
        try {
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("29")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("2")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2001")

            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectName() {
        val observer = Observer<String> {}

        try {
            viewModel.firstName.observeForever(observer)
            val name = mock(Editable::class.java)

            `when`(name.toString()).thenReturn("se")

            viewModel.onFirstNameChanged(name)

            assertEquals(FirstNameUiEvent.OnTextValid, viewModel.firstNameEvents.value)

        } finally {
            viewModel.firstName.removeObserver(observer)
        }
    }

    @Test
    fun secondCheckCorrectName() {
        val observer = Observer<String> {}

        try {
            viewModel.firstName.observeForever(observer)
            val name = mock(Editable::class.java)

            `when`(name.toString()).thenReturn("Serhii")

            viewModel.onFirstNameChanged(name)

            assertEquals(null, viewModel.firstNameEvents.value)

        } finally {
            viewModel.firstName.removeObserver(observer)
        }
    }

    @Test
    fun checkIncorrectName() {
        val observer = Observer<String> {}

        try {
            viewModel.firstName.observeForever(observer)
            val name = mock(Editable::class.java)

            `when`(name.toString()).thenReturn("")

            viewModel.onFirstNameChanged(name)

            assertEquals(FirstNameUiEvent.OnTextEmpty, viewModel.firstNameEvents.value)

        } finally {
            viewModel.firstName.removeObserver(observer)
        }
    }

    @Test
    fun checkDefaultName() {
        val observer = Observer<String> {}

        try {
            viewModel.firstName.observeForever(observer)
            val name = mock(Editable::class.java)

            `when`(name.toString()).thenReturn("s")

            viewModel.onFirstNameChanged(name)

            assertEquals(null, viewModel.firstNameEvents.value)

        } finally {
            viewModel.firstName.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectEmail() {
        val observer = Observer<String> {}

        try {
            viewModel.email.observeForever(observer)
            val email = mock(Editable::class.java)

            `when`(email.toString()).thenReturn("ser@gmail.com")

            viewModel.onEmailChanged(email)

            assertEquals(EmailUiEvent.OnTextValid, viewModel.emailEvents.value)

        } finally {
            viewModel.email.removeObserver(observer)
        }
    }

    @Test
    fun checkEmptyEmail() {
        val observer = Observer<String> {}

        try {
            viewModel.email.observeForever(observer)
            val email = mock(Editable::class.java)

            `when`(email.toString()).thenReturn("")

            viewModel.onEmailChanged(email)

            assertEquals(EmailUiEvent.OnTextEmpty, viewModel.emailEvents.value)

        } finally {
            viewModel.email.removeObserver(observer)
        }
    }

    @Test
    fun checkNotReadyEmail() {
        val observer = Observer<String> {}

        try {
            viewModel.email.observeForever(observer)
            val email = mock(Editable::class.java)

            `when`(email.toString()).thenReturn("s@gmailcom")

            viewModel.onEmailChanged(email)

            assertEquals(null, viewModel.emailEvents.value)

        } finally {
            viewModel.email.removeObserver(observer)
        }
    }

    @Test
    fun checkCorrectPassword() {
        val observer = Observer<String> {}

        try {
            viewModel.password.observeForever(observer)
            val password = mock(Editable::class.java)

            `when`(password.toString()).thenReturn("12345678")

            viewModel.onPasswordChanged(password)

            assertEquals(PasswordUiEvent.OnTextValid, viewModel.passwordEvents.value)

        } finally {
            viewModel.password.removeObserver(observer)
        }
    }

    @Test
    fun secondCheckCorrectPassword() {
        val observer = Observer<String> {}

        try {
            viewModel.password.observeForever(observer)
            val password = mock(Editable::class.java)

            `when`(password.toString()).thenReturn("123456789")

            viewModel.onPasswordChanged(password)

            assertEquals(null, viewModel.passwordEvents.value)

        } finally {
            viewModel.password.removeObserver(observer)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun correctData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        val testUsecase = mock(RegisterUserUsecase::class.java)
        val testViewModel = RegistrationViewModel(testUsecase)


        try {
            testViewModel.firstName.observeForever(observerString)
            testViewModel.lastName.observeForever(observerString)
            testViewModel.email.observeForever(observerString)
            testViewModel.password.observeForever(observerString)
            testViewModel.dateDays.observeForever(observerInt)
            testViewModel.dateMonthes.observeForever(observerInt)
            testViewModel.dateYears.observeForever(observerInt)
            testViewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            testViewModel.onFirstNameChanged(name)
            testViewModel.onLastNameChanged(lastname)
            testViewModel.onEmailChanged(email)
            testViewModel.onPasswordChanged(password)
            testViewModel.maleClicked()
            testViewModel.daysOnTextChangeListener.afterTextChanged(day)
            testViewModel.monthesOnTextChangeListener.afterTextChanged(month)
            testViewModel.yearsOnTextChangeListener.afterTextChanged(year)

            testViewModel.registrationIsClicked()

            verify(testUsecase, times(1)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectFirstNameData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("s")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectLastNameData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("s")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectEmailData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmailcom")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectPasswordData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("1234567")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectDayData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("0")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectMonthData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("0")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("2002")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun incorrectYearData() = runBlockingTest {
        val observerInt = Observer<Int> {}
        val observerString = Observer<String> {}
        val observerEvent = Observer<RegistrationEvent> {}

        try {
            viewModel.firstName.observeForever(observerString)
            viewModel.lastName.observeForever(observerString)
            viewModel.email.observeForever(observerString)
            viewModel.password.observeForever(observerString)
            viewModel.dateDays.observeForever(observerInt)
            viewModel.dateMonthes.observeForever(observerInt)
            viewModel.dateYears.observeForever(observerInt)
            viewModel.registerEvents.observeForever(observerEvent)


            val name = mock(Editable::class.java)
            `when`(name.toString()).thenReturn("se")

            val lastname = mock(Editable::class.java)
            `when`(lastname.toString()).thenReturn("se")

            val email = mock(Editable::class.java)
            `when`(email.toString()).thenReturn("sesdasdsadr@gmail.com")

            val password = mock(Editable::class.java)
            `when`(password.toString()).thenReturn("12345678")

            val day = mock(Editable::class.java)
            `when`(day.toString()).thenReturn("3")

            val month = mock(Editable::class.java)
            `when`(month.toString()).thenReturn("1")

            val year = mock(Editable::class.java)
            `when`(year.toString()).thenReturn("0")

            viewModel.onFirstNameChanged(name)
            viewModel.onLastNameChanged(lastname)
            viewModel.onEmailChanged(email)
            viewModel.onPasswordChanged(password)
            viewModel.maleClicked()
            viewModel.daysOnTextChangeListener.afterTextChanged(day)
            viewModel.monthesOnTextChangeListener.afterTextChanged(month)
            viewModel.yearsOnTextChangeListener.afterTextChanged(year)

            viewModel.registrationIsClicked()

            verify(usecase, times(0)).execute(any(), any())

        } finally {
            viewModel.firstName.removeObserver(observerString)
            viewModel.lastName.removeObserver(observerString)
            viewModel.email.removeObserver(observerString)
            viewModel.password.removeObserver(observerString)
            viewModel.dateDays.removeObserver(observerInt)
            viewModel.dateMonthes.removeObserver(observerInt)
            viewModel.dateYears.removeObserver(observerInt)
            viewModel.registerEvents.removeObserver(observerEvent)
        }
    }
}