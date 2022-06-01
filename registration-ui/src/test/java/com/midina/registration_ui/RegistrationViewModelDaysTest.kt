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
class RegistrationViewModelDaysTest {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var usecaseRegist: RegisterUserUsecase
    private lateinit var usecaseRoomDatabase: WriteToRoomDatabaseUsecase

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecaseRegist = mockk(relaxed = true)
        usecaseRoomDatabase = mockk(relaxed = true)
        viewModel = RegistrationViewModel(usecaseRegist, usecaseRoomDatabase, rule.dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkValidDaysChanged() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)
        val dayEditable = mockk<Editable>(relaxed = true)

        every { yearEditable.toString() } returns "2020"
        every { monthEditable.toString() } returns "09"
        every { dayEditable.toString() } returns "01"


        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)


        assertEquals(1, viewModel.dateDays.value)
        assertEquals(DaysUiEvent.OnFinish, viewModel.daysEvents.value)
        assertEquals(State.CORRECT, viewModel.dateState.value)
    }

    @Test
    fun checkOnProcessDaysChanged() {
        val dayEditable = mockk<Editable>(relaxed = true)

        every { dayEditable.toString() } returns "1"

        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)

        assertEquals(1, viewModel.dateDays.value)
        assertEquals(DaysUiEvent.OnTextEmpty, viewModel.daysEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun checkEmptyDaysChanged() {
        val dayEditable = mockk<Editable>(relaxed = true)

        every { dayEditable.toString() } returns ""

        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)

        assertEquals(0, viewModel.dateDays.value)
        assertEquals(DaysUiEvent.OnTextEmpty, viewModel.daysEvents.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
    }

    @Test
    fun `check days in monthes with 31days`() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)
        val dayEditable = mockk<Editable>(relaxed = true)
        val array = arrayOf(
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        )
        val expectedResult = arrayListOf(
            State.CORRECT,
            State.ERROR,
            State.CORRECT,
            State.ERROR,
            State.CORRECT,
            State.ERROR,
            State.CORRECT,
            State.CORRECT,
            State.ERROR,
            State.CORRECT,
            State.ERROR,
            State.CORRECT
        )
        val resultArray = arrayListOf<State>()

        every { yearEditable.toString() } returns "2020"
        every { dayEditable.toString() } returns "31"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)

        for (month in 0 until 12) {
            every { monthEditable.toString() } returns array[month]
            viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
            resultArray.add(viewModel.dateState.value)
        }

        assertEquals(31, viewModel.dateDays.value)
        assertEquals(expectedResult, resultArray)
    }

    @Test
    fun `check days in monthes with 30days`() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)
        val dayEditable = mockk<Editable>(relaxed = true)
        val array = arrayOf(
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
        )
        val expectedResult = arrayListOf(
            State.CORRECT,
            State.ERROR,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.CORRECT
        )
        val resultArray = arrayListOf<State>()

        every { yearEditable.toString() } returns "2020"
        every { dayEditable.toString() } returns "30"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)

        for (month in 0 until 12) {
            every { monthEditable.toString() } returns array[month]
            viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)
            resultArray.add(viewModel.dateState.value)
        }

        assertEquals(30, viewModel.dateDays.value)
        assertEquals(expectedResult, resultArray)
    }

    @Test
    fun `check days in february in leap year`() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)
        val dayEditable = mockk<Editable>(relaxed = true)

        val array = arrayOf("27", "28", "29", "30", "31")

        val expectedResult = arrayListOf(
            State.CORRECT,
            State.CORRECT,
            State.CORRECT,
            State.ERROR,
            State.ERROR
        )
        val resultArray = arrayListOf<State>()

        every { yearEditable.toString() } returns "2020"
        every { monthEditable.toString() } returns "02"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)


        for (i in array.indices) {
            every { dayEditable.toString() } returns array[i]
            viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)
            resultArray.add(viewModel.dateState.value)
        }

        assertEquals(2, viewModel.dateMonthes.value)
        assertEquals(expectedResult, resultArray)
    }

    @Test
    fun `check days in february in not leap year`() {
        val yearEditable = mockk<Editable>(relaxed = true)
        val monthEditable = mockk<Editable>(relaxed = true)
        val dayEditable = mockk<Editable>(relaxed = true)

        val array = arrayOf("27", "28", "29", "30", "31")

        val expectedResult = arrayListOf(
            State.CORRECT,
            State.CORRECT,
            State.ERROR,
            State.ERROR,
            State.ERROR
        )
        val resultArray = arrayListOf<State>()

        every { yearEditable.toString() } returns "2021"
        every { monthEditable.toString() } returns "02"

        viewModel.yearsOnTextChangeListener.afterTextChanged(yearEditable)
        viewModel.monthesOnTextChangeListener.afterTextChanged(monthEditable)

        for (i in array.indices) {
            every { dayEditable.toString() } returns array[i]
            viewModel.daysOnTextChangeListener.afterTextChanged(dayEditable)
            resultArray.add(viewModel.dateState.value)
        }

        assertEquals(2, viewModel.dateMonthes.value)
        assertEquals(expectedResult, resultArray)
    }


    @Test
    fun hasFocus() {
        val view: View = mockk(relaxed = true)

        viewModel.daysOnFocusListener.onFocusChange(view, true)

        assertEquals(0, viewModel.dateDays.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(DaysUiEvent.OnTextEmpty, viewModel.daysEvents.value)
    }


    @Test
    fun emptyNoFocus() {
        val view: View = mockk(relaxed = true)
        mockkStatic(TextUtils::class)

        every { TextUtils.isDigitsOnly(any()) } answers { false }

        viewModel.daysOnFocusListener.onFocusChange(view, false)

        assertEquals(0, viewModel.dateDays.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(DaysUiEvent.OnTextEmpty, viewModel.daysEvents.value)
    }

    @Test
    fun onlyDigitsDataNoFocus() {
        val view: View = mockk(relaxed = true)
        val editable: Editable = mockk(relaxed = true)
        mockkStatic(TextUtils::class)

        every { TextUtils.isDigitsOnly(any()) } answers { true }

        every { editable.toString() } returns "35"

        viewModel.daysOnTextChangeListener.afterTextChanged(editable)
        viewModel.daysOnFocusListener.onFocusChange(view, false)

        assertEquals(35, viewModel.dateDays.value)
        assertEquals(State.DEFAULT, viewModel.dateState.value)
        assertEquals(DaysUiEvent.OnFinish, viewModel.daysEvents.value)
    }
}