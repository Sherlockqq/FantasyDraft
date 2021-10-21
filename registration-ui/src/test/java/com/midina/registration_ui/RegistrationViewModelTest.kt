package com.midina.registration_ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.midina.core_ui.ui.State
import com.midina.registration_domain.usecase.RegisterUserUsecase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class RegistrationViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: RegistrationViewModel

    @Before
    fun setupViewModel() {
        val usecase = mock(RegisterUserUsecase::class.java)
        viewModel = RegistrationViewModel(usecase)
    }

    @Test
    fun correctEmail() {
       assertEquals(true,viewModel.isEmail("ser@gmail.com"))
    }

    @Test
    fun incorrectEmail() {
        assertEquals(false,viewModel.isEmail("ser@gmail"))
    }

    @Test
    fun checkInCorrectDay() {
        val observer = Observer<Int> {}
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("0")
            viewModel.monthesHandler("10")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("3")
            viewModel.monthesHandler("0")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("3")
            viewModel.monthesHandler("3")
            viewModel.yearsHandler("2021")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("23")
            viewModel.monthesHandler("10")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("31")
            viewModel.monthesHandler("10")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("31")
            viewModel.monthesHandler("1")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("32")
            viewModel.monthesHandler("1")
            viewModel.yearsHandler("2002")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("29")
            viewModel.monthesHandler("2")
            viewModel.yearsHandler("2000")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("30")
            viewModel.monthesHandler("2")
            viewModel.yearsHandler("2000")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("28")
            viewModel.monthesHandler("2")
            viewModel.yearsHandler("2001")

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
        try{
            viewModel.dateDays.observeForever(observer)
            viewModel.dateMonthes.observeForever(observer)
            viewModel.dateYears.observeForever(observer)

            viewModel.daysHandler("29")
            viewModel.monthesHandler("2")
            viewModel.yearsHandler("2001")

            assertEquals(State.ERROR, viewModel.dateState)

        } finally {
            viewModel.dateDays.removeObserver(observer)
            viewModel.dateMonthes.removeObserver(observer)
            viewModel.dateYears.removeObserver(observer)
        }
    }

}