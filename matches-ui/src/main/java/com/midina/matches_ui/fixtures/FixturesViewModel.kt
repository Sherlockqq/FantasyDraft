package com.midina.matches_ui.fixtures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.core_domain.usecases.GetSeasonUsecase
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetCurrentTourUsecase
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.ArrayList

//2021-08-13T19:00:00+00:00

class FixturesViewModel @Inject constructor(
    private val getSeasonUsecase: GetSeasonUsecase,
    private val getMatchesScheduleUsecase: GetMatchesScheduleUsecase,
    private val getCurrentTourUsecase: GetCurrentTourUsecase
) : ViewModel() {

    private var matchesMap: Map<Int, ArrayList<MatchSchedule>> = mutableMapOf()

    private val _season = MutableStateFlow(0)
    val season: StateFlow<Int>
        get() = _season.asStateFlow()

    private val _currentTour = MutableStateFlow(0)
    val currentTour: StateFlow<Int>
        get() = _currentTour.asStateFlow()

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    init {
        seasonLoad()
    }

    private fun seasonLoad() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSeasonUsecase.execute()

            when (result) {
                com.midina.core_domain.model.ResultEvent.EmptyState -> _events.value =
                    UiEvent.EmptyState
                com.midina.core_domain.model.ResultEvent.Error -> _events.value = UiEvent.Error
                is com.midina.core_domain.model.ResultEvent.Success -> {
                    _season.value = result.value
                    matchesLoad()
                }
            }
        }
    }

    private suspend fun matchesLoad() {
        val result = getMatchesScheduleUsecase.execute(season.value)

        when (result) {
            is ResultEvent.Success -> {
                matchesMap = result.value
                currentTourLoad()
            }
            ResultEvent.Error -> _events.value = UiEvent.Error
            ResultEvent.EmptyState -> _events.value = UiEvent.Error
        }
    }

    private suspend fun currentTourLoad() {
        val result = getCurrentTourUsecase.execute(season.value)

        when (result) {
            is ResultEvent.Success -> {
                if (result.value == 0) {
                    _currentTour.value = matchesMap.size
                } else {
                    _currentTour.value = result.value
                }
                _events.value = UiEvent.Success(matchesMap)
            }
            ResultEvent.Error -> _events.value = UiEvent.Error
            ResultEvent.EmptyState -> _events.value = UiEvent.EmptyState
        }
    }
}


sealed class UiEvent {
    class Success(val matches: Map<Int, ArrayList<MatchSchedule>>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}