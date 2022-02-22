package com.midina.login_ui

import com.midina.login_ui.LoginViewModel
import com.midina.login_domain.usecase.SigningInUsecase
import org.junit.Assert.assertEquals
import org.junit.Test


class LoginViewModelTest {

    lateinit var viewModel: com.midina.login_ui.LoginViewModel
    lateinit var signingInUsecase: SigningInUsecase

    @org.junit.Before
    fun setUp() {
        signingInUsecase = Mockito
    }

    @org.junit.After
    fun tearDown() {
    }

    @Test
    fun test() {
        assertEquals(1, 2)
    }
}