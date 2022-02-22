package com.midina.registration_ui

import android.text.Editable
import android.view.View
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class RegistrationViewModelFirstNameTest{

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
    fun checkValidFirstNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "UA"

        viewModel.onFirstNameChanged(editable)

        assertEquals("UA", viewModel.firstName.value)
        assertEquals(State.CORRECT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextValid, viewModel.firstNameEvents.value)
    }

    @Test
    fun checkDefaultFirstNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "1"

        viewModel.onFirstNameChanged(editable)

        assertEquals("1", viewModel.firstName.value)
        assertEquals(State.DEFAULT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextEmpty, viewModel.firstNameEvents.value)
    }

    @Test
    fun checkEmptyFirstNameChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns ""

        viewModel.onFirstNameChanged(editable)

        assertEquals("", viewModel.firstName.value)
        assertEquals(State.DEFAULT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextEmpty, viewModel.firstNameEvents.value)
    }

    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.firstNameOnFocusListener.onFocusChange(view, true)

        assertEquals("", viewModel.firstName.value)
        assertEquals(State.DEFAULT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextEmpty, viewModel.firstNameEvents.value)
    }

    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.firstNameOnFocusListener.onFocusChange(view, false)

        assertEquals("", viewModel.firstName.value)
        assertEquals(State.DEFAULT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextEmpty, viewModel.firstNameEvents.value)
    }

    @Test
    fun invalidDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "s"

        viewModel.onFirstNameChanged(editable)
        viewModel.firstNameOnFocusListener.onFocusChange(view, false)

        assertEquals("s", viewModel.firstName.value)
        assertEquals(State.ERROR, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextInvalid, viewModel.firstNameEvents.value)
    }

    @Test
    fun correctDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)

        every { editable.toString() } returns "se"

        viewModel.onFirstNameChanged(editable)
        viewModel.firstNameOnFocusListener.onFocusChange(view, false)

        assertEquals("se", viewModel.firstName.value)
        assertEquals(State.CORRECT, viewModel.firstNameState.value)
        assertEquals(FirstNameUiEvent.OnTextValid, viewModel.firstNameEvents.value)

    }
}