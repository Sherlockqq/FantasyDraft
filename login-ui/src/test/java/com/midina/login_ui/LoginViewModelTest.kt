package com.midina.login_ui

import android.text.Editable
import com.midina.login_domain.model.ResultEvent
import com.midina.login_domain.usecase.SigningInUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var usecase: SigningInUsecase
    private lateinit var email: Editable
    private lateinit var password: Editable

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecase = Mockito.mock(SigningInUsecase::class.java)
        email = Mockito.mock(Editable::class.java)
        password = Mockito.mock(Editable::class.java)

        viewModel = LoginViewModel(usecase, rule.dispatcher)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun invalidateDataTest() = rule.runBlockingTest {
        Mockito.`when`(email.toString()).thenReturn("qw")
        Mockito.`when`(password.toString()).thenReturn("qw3")
        Mockito.`when`(usecase.execute(email.toString(),password.toString()))
            .thenReturn(ResultEvent.InvalidateData)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.signInClicked()

        assertEquals(LoginEvent.OnError, viewModel.loginEvents.value)
    }

    @Test
    fun successDataTest() = rule.runBlockingTest {
        Mockito.`when`(email.toString()).thenReturn("qw")
        Mockito.`when`(password.toString()).thenReturn("qw3")
        Mockito.`when`(usecase.execute(email.toString(),password.toString()))
            .thenReturn(ResultEvent.Success)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.signInClicked()

        assertEquals(LoginEvent.OnSuccess, viewModel.loginEvents.value)
    }

    @Test
    fun errorDataTest() = rule.runBlockingTest {
        Mockito.`when`(email.toString()).thenReturn("qw")
        Mockito.`when`(password.toString()).thenReturn("qw3")
        Mockito.`when`(usecase.execute(email.toString(),password.toString()))
            .thenReturn(ResultEvent.Error)

        viewModel.onEmailChanged(email)
        viewModel.onPasswordChanged(password)

        viewModel.signInClicked()

        assertEquals(LoginEvent.OnError, viewModel.loginEvents.value)
    }
}