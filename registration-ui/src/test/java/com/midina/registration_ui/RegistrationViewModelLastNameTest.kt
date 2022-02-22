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
class RegistrationViewModelLastNameTest {
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
    fun checkValidLastNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "UA"

        viewModel.onLastNameChanged(editable)

        assertEquals("UA", viewModel.lastName.value)
        assertEquals(State.CORRECT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextValid, viewModel.lastNameEvents.value)
    }

    @Test
    fun checkDefaultLastNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "1"

        viewModel.onLastNameChanged(editable)

        assertEquals("1", viewModel.lastName.value)
        assertEquals(State.DEFAULT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextEmpty, viewModel.lastNameEvents.value)
    }

    @Test
    fun checkErrorLastNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns ""

        viewModel.onLastNameChanged(editable)

        assertEquals("", viewModel.lastName.value)
        assertEquals(State.DEFAULT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextEmpty, viewModel.lastNameEvents.value)
    }

    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.lastNameOnFocusListener.onFocusChange(view, true)

        assertEquals("", viewModel.lastName.value)
        assertEquals(State.DEFAULT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextEmpty, viewModel.lastNameEvents.value)
    }

    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.lastNameOnFocusListener.onFocusChange(view, false)

        assertEquals("", viewModel.lastName.value)
        assertEquals(State.DEFAULT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextEmpty, viewModel.lastNameEvents.value)
    }

    @Test
    fun invalidDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "s"

        viewModel.onLastNameChanged(editable)
        viewModel.lastNameOnFocusListener.onFocusChange(view, false)

        assertEquals("s", viewModel.lastName.value)
        assertEquals(State.ERROR, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextInvalid, viewModel.lastNameEvents.value)
    }

    @Test
    fun correctDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "se"

        viewModel.onLastNameChanged(editable)
        viewModel.lastNameOnFocusListener.onFocusChange(view, false)

        assertEquals("se", viewModel.lastName.value)
        assertEquals(State.CORRECT, viewModel.lastNameState.value)
        assertEquals(LastNameUiEvent.OnTextValid, viewModel.lastNameEvents.value)

    }


}