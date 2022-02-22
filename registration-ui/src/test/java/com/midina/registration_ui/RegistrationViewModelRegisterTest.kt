package com.midina.registration_ui

import android.text.Editable
import android.util.Log
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelRegisterTest {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var usecase: RegisterUserUsecase
    private lateinit var firstNameEditable: Editable
    private lateinit var lastNameEditable: Editable
    private lateinit var emailEditable: Editable
    private lateinit var passwordEditable: Editable
    private lateinit var dayEditable: Editable
    private lateinit var monthEditable: Editable
    private lateinit var yearEditable: Editable
    private lateinit var usecaseDatabase: WriteToDatabaseUsecase

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecase = mockk(relaxed = true)
        firstNameEditable = mockk(relaxed = true)
        lastNameEditable = mockk(relaxed = true)
        emailEditable = mockk(relaxed = true)
        passwordEditable = mockk(relaxed = true)
        dayEditable = mockk(relaxed = true)
        monthEditable = mockk(relaxed = true)
        yearEditable = mockk(relaxed = true)
        usecaseDatabase = mockk(relaxed = true)
        mockkStatic(Log::class)

        viewModel = RegistrationViewModel(usecase, usecaseDatabase, rule.dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun registrationIsSuccess() = rule.runBlockingTest {

        every { firstNameEditable.toString() } returns "se"
        every { lastNameEditable.toString() } returns "se"
        every { emailEditable.toString() } returns "se@g.c"
        every { passwordEditable.toString() } returns "12345678"
        every { dayEditable.toString() } returns "01"
        every { monthEditable.toString() } returns "09"
        every { yearEditable.toString() } returns "2020"
        every { Log.d(any(), any())} returns 0
        coEvery { usecase.execute(any(), any()) } returns ResultEvent.Success
        coEvery { usecaseDatabase.execute(any()) } returns ResultEvent.Success


        viewModel.onFirstNameChanged(firstNameEditable)
        viewModel.onLastNameChanged(lastNameEditable)
        viewModel.onEmailChanged(emailEditable)
        viewModel.onPasswordChanged(passwordEditable)
        viewModel.maleClicked()
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)

        viewModel.registrationIsClicked()

        assertTrue(viewModel.registerEvents.value is RegistrationEvent.OnSuccess)
    }

    @Test
    fun registrationIsError() = rule.runBlockingTest {

        every { firstNameEditable.toString() } returns "se"
        every { lastNameEditable.toString() } returns "se"
        every { emailEditable.toString() } returns "se@g.c"
        every { passwordEditable.toString() } returns "12345678"
        every { dayEditable.toString() } returns "01"
        every { monthEditable.toString() } returns "09"
        every { yearEditable.toString() } returns "2020"
        coEvery { usecase.execute(any(), any()) } returns ResultEvent.Error


        viewModel.onFirstNameChanged(firstNameEditable)
        viewModel.onLastNameChanged(lastNameEditable)
        viewModel.onEmailChanged(emailEditable)
        viewModel.onPasswordChanged(passwordEditable)
        viewModel.maleClicked()
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)

        viewModel.registrationIsClicked()

        assertTrue(viewModel.registerEvents.value is RegistrationEvent.OnError)
    }

    @Test
    fun registrationDataIsNotCorrect() = rule.runBlockingTest {

        every { firstNameEditable.toString() } returns "se"
        every { lastNameEditable.toString() } returns "se"
        every { emailEditable.toString() } returns "se@g.c"
        every { passwordEditable.toString() } returns "12345678"
        every { dayEditable.toString() } returns "01"
        every { monthEditable.toString() } returns "09"
        every { yearEditable.toString() } returns "2030"

        viewModel.onFirstNameChanged(firstNameEditable)
        viewModel.onLastNameChanged(lastNameEditable)
        viewModel.onEmailChanged(emailEditable)
        viewModel.onPasswordChanged(passwordEditable)
        viewModel.maleClicked()
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)

        viewModel.registrationIsClicked()

        assertTrue(viewModel.registerEvents.value is RegistrationEvent.OnError)
        assertEquals(State.CORRECT, viewModel.firstNameState.value)
        assertEquals(State.CORRECT, viewModel.lastNameState.value)
        assertEquals(State.CORRECT, viewModel.emailState.value)
        assertEquals(State.CORRECT, viewModel.passwordState.value)
        assertEquals(State.CORRECT, viewModel.genderState.value)
        assertEquals(State.ERROR, viewModel.dateState.value)
    }


    @Test
    fun registrationEmptyData() = rule.runBlockingTest {

        viewModel.registrationIsClicked()

        assertTrue(viewModel.registerEvents.value is RegistrationEvent.OnError)
        assertEquals(State.ERROR, viewModel.firstNameState.value)
        assertEquals(State.ERROR, viewModel.lastNameState.value)
        assertEquals(State.ERROR, viewModel.emailState.value)
        assertEquals(State.ERROR, viewModel.passwordState.value)
        assertEquals(State.ERROR, viewModel.genderState.value)
        assertEquals(State.ERROR, viewModel.dateState.value)
    }
}