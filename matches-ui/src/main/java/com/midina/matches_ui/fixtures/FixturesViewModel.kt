package com.midina.matches_ui.fixtures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getMatchesScheduleUsecase: GetMatchesScheduleUsecase,
    private val getCurrentTourUsecase: GetCurrentTourUsecase
) : ViewModel() {

    private var matchesMap: Map<Int, ArrayList<MatchSchedule>> = mutableMapOf()

    private val _currentTour = MutableStateFlow(0)
    val currentTour: StateFlow<Int>
        get() = _currentTour.asStateFlow()

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    init {
        matchesLoad()
    }

    private fun matchesLoad() {

        viewModelScope.launch(Dispatchers.IO) {

            val result = getMatchesScheduleUsecase.execute()

            when (result) {
                is ResultEvent.Success -> {
                    matchesMap = result.value
                    val currentSeason = result.value[1]!![0].date.substring(0,4)
                    val tourResult = getCurrentTourUsecase.execute(currentSeason.toInt())

                    when (tourResult) {
                        is ResultEvent.Success -> {
                            if (tourResult.value == 0) {
                                // The last tour, when league is finished
                                _currentTour.value = matchesMap.size
                            } else {
                                _currentTour.value = tourResult.value
                            }
                            _events.value = UiEvent.Success(matchesMap)
                        }
                        is ResultEvent.Error -> _events.value = UiEvent.Error
                    }
                }
                is ResultEvent.Error -> _events.value = UiEvent.Error
            }
        }
    }

}


sealed class UiEvent {
    class Success(val matches: Map<Int, ArrayList<MatchSchedule>>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}