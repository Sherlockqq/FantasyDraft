package com.midina.registration_ui

import android.text.Editable
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
class RegistrationViewModelYearTest {

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
    fun checkValidYearChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "2021"

        viewModel.yearsOnTextChangeListener.afterTextChanged(editable)

        assertEquals(2021, viewModel.dateYears.value)
        assertEquals(YearsUiEvent.OnFinish, viewModel.yearsEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkDefaultYearChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "322"

        viewModel.yearsOnTextChangeListener.afterTextChanged(editable)

        assertEquals(322, viewModel.dateYears.value)
        assertEquals(YearsUiEvent.OnTextEmpty, viewModel.yearsEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkInvalidYearChanged() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "1500"

        viewModel.yearsOnTextChangeListener.afterTextChanged(editable)

        assertEquals(1500, viewModel.dateYears.value)
        assertEquals(YearsUiEvent.OnFinish, viewModel.yearsEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkYearEmpty() {
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns ""

        viewModel.yearsOnTextChangeListener.afterTextChanged(editable)

        assertEquals(0, viewModel.dateYears.value)
        assertEquals(YearsUiEvent.OnTextEmpty, viewModel.yearsEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }
}