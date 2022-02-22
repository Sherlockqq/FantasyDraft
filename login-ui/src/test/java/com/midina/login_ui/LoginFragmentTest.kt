package com.midina.login_ui

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoginFragmentTest {

    private lateinit var fragment: LoginFragment
    private lateinit var scenario: FragmentScenario<LoginFragment>


    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }


    @Test
    fun emailEditTypeTest() {
        scenario.onFragment{
            assertEquals(R.layout.fragment_login, it.layoutId)
         }
    }

}