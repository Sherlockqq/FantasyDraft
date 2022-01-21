package com.midina.engfixtures_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.engfixtures_domain.model.Match
import com.midina.engfixtures_domain.model.ResultEvent
import com.midina.engfixtures_domain.usecases.GetFixturesUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EngFixturesViewModel @Inject constructor(
    private val getFixturesUsecase: GetFixturesUsecase
) : ViewModel() {

    private var matchesMap: Map<Int, ArrayList<Match>> = mutableMapOf()

    private val _events = MutableStateFlow<UiEvent>(UiEvent.EmptyState)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    init {
        getFixtures()
    }

    private fun getFixtures() {
        viewModelScope.launch {
            val result = getFixturesUsecase.execute()
            when (result) {
                is ResultEvent.Success -> {
                    matchesMap = result.value
                    _events.value = UiEvent.Success(result.value)
                }
                is ResultEvent.Error -> {
                    _events.value = UiEvent.Error
                }
            }
        }
    }

}

sealed class UiEvent {
    class Success(val matches: Map<Int, ArrayList<Match>>) : UiEvent()
    object EmptyState : UiEvent()
    object Error : UiEvent()
}