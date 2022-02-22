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
class RegistrationViewModelEmailTest {

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
    fun checkValidEmailChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "smidina@gmail.c"

        viewModel.onEmailChanged(editable)

        assertEquals("smidina@gmail.c", viewModel.email.value)
        assertEquals(State.CORRECT, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextValid, viewModel.emailEvents.value)
    }

    @Test
    fun checkDefaultEmailChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "smidina@gm"

        viewModel.onEmailChanged(editable)

        assertEquals("smidina@gm", viewModel.email.value)
        assertEquals(State.DEFAULT, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextEmpty, viewModel.emailEvents.value)
    }

    @Test
    fun checkErrorEmailChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns ""

        viewModel.onEmailChanged(editable)

        assertEquals("", viewModel.email.value)
        assertEquals(State.DEFAULT, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextEmpty, viewModel.emailEvents.value)
    }

    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.emailOnFocusListener.onFocusChange(view, true)

        assertEquals("", viewModel.email.value)
        assertEquals(State.DEFAULT, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextEmpty, viewModel.emailEvents.value)
    }


    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.emailOnFocusListener.onFocusChange(view, false)

        assertEquals("", viewModel.email.value)
        assertEquals(State.ERROR, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextInvalid, viewModel.emailEvents.value)
    }

    @Test
    fun invalidDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "s"

        viewModel.onEmailChanged(editable)
        viewModel.emailOnFocusListener.onFocusChange(view, false)

        assertEquals("s", viewModel.email.value)
        assertEquals(State.ERROR, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextInvalid, viewModel.emailEvents.value)
    }

    @Test
    fun correctDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "se@g.c"

        viewModel.onEmailChanged(editable)
        viewModel.emailOnFocusListener.onFocusChange(view, false)

        assertEquals("se@g.c", viewModel.email.value)
        assertEquals(State.CORRECT, viewModel.emailState.value)
        assertEquals(EmailUiEvent.OnTextValid, viewModel.emailEvents.value)
    }
}