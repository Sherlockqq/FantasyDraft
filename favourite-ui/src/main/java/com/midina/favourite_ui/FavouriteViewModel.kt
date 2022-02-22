package com.midina.favourite_ui

import androidx.lifecycle.ViewModel
import com.midina.favourite_domain.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class FavouriteViewModel @Inject constructor() : ViewModel() {

    private val _teams = MutableStateFlow( mutableListOf<Team>())
    val teams: StateFlow<List<Team>>
        get() = _teams

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    private val teamNames = arrayListOf(
        "Львов",
        "Верес",
        "Шахтер Донецк",
        "Металлист 1925",
        "Десна",
        "Заря",
        "Ворскла",
        "Динамо Киев",
        "Мариуполь",
        "Колос К",
        "Ингулец",
        "Рух Львов",
        "Черноморец",
        "Александрия",
        "Днепр-1",
        "Минай"
    )

    init {
        getTeams()
    }

    private fun getTeams() {
        teamNames.forEach { name ->
            _teams.value.add(Team(name))
        }
        _events.value = UiEvent.Success(teams.value)
    }
}


sealed class UiEvent {
    class Success(val team: List<Team>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}