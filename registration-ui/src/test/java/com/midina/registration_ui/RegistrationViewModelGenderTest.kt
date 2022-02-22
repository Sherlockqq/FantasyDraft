package com.midina.registration_ui

import com.midina.registration_domain.model.Gender
import com.midina.registration_domain.usecase.RegisterUserUsecase
import com.midina.registration_domain.usecase.WriteToDatabaseUsecase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RegistrationViewModelGenderTest {

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
    fun maleClicked() {

        assertEquals(Gender.UNSPECIFIED, viewModel.gender.value)
        assertEquals(State.DEFAULT, viewModel.genderState.value)
        
        viewModel.maleClicked()

        assertEquals(Gender.MALE, viewModel.gender.value)
        assertEquals(State.CORRECT, viewModel.genderState.value)
    }

    @Test
    fun femaleClicked() {
        assertEquals(Gender.UNSPECIFIED, viewModel.gender.value)
        assertEquals(State.DEFAULT, viewModel.genderState.value)
        viewModel.femaleClicked()

        assertEquals(Gender.FEMALE, viewModel.gender.value)
        assertEquals(State.CORRECT, viewModel.genderState.value)
    }

    @Test
    fun unspecifiedClicked() {
        assertEquals(Gender.UNSPECIFIED, viewModel.gender.value)
        assertEquals(State.DEFAULT, viewModel.genderState.value)
        viewModel.unspecifiedClicked()

        assertEquals(Gender.UNSPECIFIED, viewModel.gender.value)
        assertEquals(State.CORRECT, viewModel.genderState.value)
    }

}