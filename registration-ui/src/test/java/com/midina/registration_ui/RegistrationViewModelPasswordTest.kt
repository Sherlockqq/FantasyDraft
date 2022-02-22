package com.midina.registration_ui

import android.text.Editable
import android.view.View
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelPasswordTest {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var usecase: RegisterUserUsecase
    private lateinit var usecaseDatabase: WriteToDatabaseUsecase

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecase = mockk(relaxed = true)
        usecaseDatabase = mockk(relaxed = true)
        viewModel = RegistrationViewModel(usecase, usecaseDatabase, rule.dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkValidPasswordChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "12345678"

        viewModel.onPasswordChanged(editable)

        assertEquals("12345678", viewModel.password.value)
        assertEquals(State.CORRECT, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextValid, viewModel.passwordEvents.value)
    }

    @Test
    fun checkDefaultPasswordChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "12345"

        viewModel.onPasswordChanged(editable)

        assertEquals("12345", viewModel.password.value)
        assertEquals(State.DEFAULT, viewModel.passwordState.value)
        assertTrue(viewModel.passwordEvents.value is PasswordUiEvent.OnProcess)
        assertEquals(
            5, (viewModel.passwordEvents.value
                    as PasswordUiEvent.OnProcess).textSize
        )
    }

    @Test
    fun checkErrorPasswordChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns ""

        viewModel.onPasswordChanged(editable)

        assertEquals("", viewModel.password.value)
        assertEquals(State.DEFAULT, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextEmpty, viewModel.passwordEvents.value)
    }


    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.passwordOnFocusListener.onFocusChange(view, true)

        assertEquals("", viewModel.password.value)
        assertEquals(State.DEFAULT, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextEmpty, viewModel.passwordEvents.value)
    }


    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.passwordOnFocusListener.onFocusChange(view, false)

        assertEquals("", viewModel.password.value)
        assertEquals(State.DEFAULT, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextEmpty, viewModel.passwordEvents.value)
    }

    @Test
    fun invalidDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "s"

        viewModel.onPasswordChanged(editable)
        viewModel.passwordOnFocusListener.onFocusChange(view, false)

        assertEquals("s", viewModel.password.value)
        assertEquals(State.ERROR, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextInvalid, viewModel.passwordEvents.value)
    }

    @Test
    fun correctDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "12345678"

        viewModel.onPasswordChanged(editable)
        viewModel.passwordOnFocusListener.onFocusChange(view, false)

        assertEquals("12345678", viewModel.password.value)
        assertEquals(State.CORRECT, viewModel.passwordState.value)
        assertEquals(PasswordUiEvent.OnTextValid, viewModel.passwordEvents.value)
    }
}