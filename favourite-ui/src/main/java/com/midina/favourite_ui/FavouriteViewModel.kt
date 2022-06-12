package com.midina.favourite_ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.core_ui.ui.BaseFragment.Companion.SEASON
import com.midina.favourite_domain.model.ResultEvent
import com.midina.favourite_domain.model.Team
import com.midina.favourite_domain.usecases.GetTeamsUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class FavouriteViewModel @Inject constructor(
    private val getTeamsUsecase: GetTeamsUsecase,
    @Named("FavouriteBundle")
    bundle: Bundle?
) : ViewModel() {

    private val _teams = MutableStateFlow(mutableListOf<Team>())
    val teams: StateFlow<List<Team>>
        get() = _teams

    private val _season = MutableStateFlow(0)
    val season: StateFlow<Int>
        get() = _season.asStateFlow()

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    init {
        _season.value = bundle?.getInt(SEASON).toString().toInt()
        teamsRequest()
    }

    private fun teamsRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getTeamsUsecase.execute(season.value)

            when (result) {
                ResultEvent.EmptyState -> _events.value = UiEvent.EmptyState
                ResultEvent.Error -> _events.value = UiEvent.Error
                is ResultEvent.Success -> {
                    _teams.value = result.value as MutableList<Team>
                    _events.value = UiEvent.Success(teams.value)
                }
            }
        }
    }
}

sealed class UiEvent {
    class Success(val team: List<Team>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}
