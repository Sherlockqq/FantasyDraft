package com.midina.matches_ui.fixtures

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecases.GetMatchesScheduleUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

//TODO parse gif when match is going

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val TOUR_SIZE = 30
private const val MATCHES_IN_TOUR = 8
private const val FIRST_MATCH_IN_TOUR = 0
private const val MATCHES_COUNT = 240

class FixturesViewModel @Inject constructor(
    private val getMatchesScheduleUsecase: GetMatchesScheduleUsecase
) : ViewModel() {

    private var matchesMap: Map<Int, ArrayList<MatchSchedule>> = mutableMapOf()
    private var dateMap: MutableMap<Int, Pair<String, String>> = mutableMapOf()

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

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
                    getDateMap()
                    val tourByDate = getTourByDate()
                    _currentTour.value = tourByDate
                    _events.value = UiEvent.Success(matchesMap)
                }
                is ResultEvent.Error -> _events.value = UiEvent.Error
            }
        }
    }

    private fun getTourByDate(): Int {

        val currentDateString = sdf.format(Date())

        var tourCount = 1
        for (index in 1..TOUR_SIZE) {

            if ((getTimeInMillis(currentDateString) -
                        getTimeInMillis(dateMap[index]?.second.toString()) > 0)
            ) {
                tourCount = index
            } else {
                if ((getTimeInMillis(currentDateString) -
                            getTimeInMillis(dateMap[index]?.first.toString()) > 0) &&
                    (getTimeInMillis(currentDateString) -
                            getTimeInMillis(dateMap[index]?.second.toString()) < 0)
                ) {
                    return index
                }
            }
        }

        return if (tourCount == TOUR_SIZE) {
            tourCount
        } else {
            tourCount + 1
        }

    }

    private fun getStringPair(first: String, last: String):
            Pair<String, String> {
        return Pair(first, last)
    }

    private fun getDateMap() {

        var matchCount = FIRST_MATCH_IN_TOUR
        var tourCount = 1
        for (index in 0 until MATCHES_COUNT) {
            matchCount++
            if (matchCount == MATCHES_IN_TOUR) {
                matchesMap[tourCount]?.get(FIRST_MATCH_IN_TOUR)?.let { first ->
                    matchesMap[tourCount]?.get(MATCHES_IN_TOUR - 1)?.let { second ->
                        dateMap[tourCount] = getStringPair(
                            first.date,
                            second.date
                        )
                    }
                }
                matchCount = FIRST_MATCH_IN_TOUR
                tourCount++
            }
        }
    }

    fun getTimeInMillis(matchTime: String): Long {
        return  sdf.parse(matchTime).time
    }
}


sealed class UiEvent {
    class Success(val matches: Map<Int, ArrayList<MatchSchedule>>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}