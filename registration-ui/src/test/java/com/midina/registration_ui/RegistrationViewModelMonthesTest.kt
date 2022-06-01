package com.midina.registration_ui

import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToRoomDatabaseUsecase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelMonthesTest {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var usecase: RegisterUserUsecase
    private lateinit var usecaseRoomDatabase: WriteToRoomDatabaseUsecase

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecase = mockk(relaxed = true)
        usecaseRoomDatabase = mockk(relaxed = true)
        viewModel = RegistrationViewModel(usecase, usecaseRoomDatabase, rule.dispatcher)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun checkValidMonthesChanged() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)

        every { yearEditable.toString() } returns "2020"
        every { monthEditable.toString() } returns "09"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)

        assertEquals(9, viewModel.dateMonthes.value)
        assertEquals(MonthesUiEvent.OnFinish, viewModel.monthesEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkInvalidMonthesChanged() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)

        every { yearEditable.toString() } returns "2020"
        every { monthEditable.toString() } returns "19"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)

        assertEquals(19, viewModel.dateMonthes.value)
        assertEquals(MonthesUiEvent.OnFinish, viewModel.monthesEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkOnProcessMonthesChanged() {
        val monthEditable = mockk<Editable>(relaxed = true)

        every { monthEditable.toString() } returns "9"

        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)

        assertEquals(9, viewModel.dateMonthes.value)
        assertEquals(MonthesUiEvent.OnTextEmpty, viewModel.monthesEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkEmptyMonthesChanged() {
        val monthEditable = mockk<Editable>(relaxed = true)

        every { monthEditable.toString() } returns ""

        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)

        assertEquals(0, viewModel.dateMonthes.value)
        assertEquals(MonthesUiEvent.OnTextEmpty, viewModel.monthesEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.monthesOnFocusListener.onFocusChange(view, true)

        assertEquals(0, viewModel.dateMonthes.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(MonthesUiEvent.OnTextEmpty, viewModel.monthesEvents.value)
    }

    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)
        mockkStatic(TextUtils::class)

        every { TextUtils.isDigitsOnly(any()) } answers { false }

        viewModel.monthesOnFocusListener.onFocusChange(view, false)

        assertEquals(0, viewModel.dateMonthes.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(MonthesUiEvent.OnTextEmpty, viewModel.monthesEvents.value)
    }

    @Test
    fun onlyDigitsDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)
        mockkStatic(TextUtils::class)

        every { TextUtils.isDigitsOnly(any()) } answers { true }
        every { editable.toString() } returns "35"

        viewModel.monthesOnTextChangeListener.afterTextChanged(editable)
        viewModel.monthesOnFocusListener.onFocusChange(view, false)

        assertEquals(35, viewModel.dateMonthes.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(MonthesUiEvent.OnFinish, viewModel.monthesEvents.value)
    }
}