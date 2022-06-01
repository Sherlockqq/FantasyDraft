package com.midina.favourite_ui

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class FavouriteViewModelTest {

    lateinit var viewModel: FavouriteViewModel

    @Before
    fun setUp() {
        viewModel = FavouriteViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun checkTeamSize() {
        if (viewModel.events.value is UiEvent.Success) {
            assertEquals(16, (viewModel.events.value as UiEvent.Success).team.size)
        }
    }

}