package com.midina.registration_data

import com.midina.registration_data.database.UserDao
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class RegisterUserRepositoryTest {

    private lateinit var dao: UserDao
    private lateinit var repository: RegisterUserRepository

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = RegisterUserRepository(dao)
    }

    @After
    fun tearDown() {
    }
}