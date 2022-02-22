package com.midina.login_ui

import android.text.InputType
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTestEspresso {



    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun emailEditTypeTest() {
        onView(withId(R.id.et_email))
            .check(matches(withInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)))
    }


    @Test
    fun testEventFragment() {

//        with(launchFragment<LoginFragment>()) {
//            onFragment { fragment ->
//                fragment.lifecycleScope.launchWhenCreated
//
//            }
//        }
//
//        onView()

    }
}