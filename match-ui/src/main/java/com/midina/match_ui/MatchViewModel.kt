package com.midina.match_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.model.Match
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.usecase.GetMatchUsecase
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Timer
import java.util.TimerTask
import java.util.Date
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

private const val DATE_PATTERN = "yyyy-MM-DD HH:mm"
private const val MINUTE = 60000L
private const val TAG = "MatchViewModel"

@SuppressLint("SimpleDateFormat")
class MatchViewModel @Inject constructor(
    private val getWeatherUsecase: GetWeatherUsecase,
    private val getMatchUsecase: GetMatchUsecase,
    private val dispatchers: CoroutineDispatcher,
    @Named("MatchBundle")
    bundle: Bundle?
) : ViewModel() {

    private val _matchId = MutableStateFlow(0)
    val matchId: StateFlow<Int>
        get() = _matchId.asStateFlow()

    private val _city = MutableStateFlow("")
    val city: StateFlow<String>
        get() = _city.asStateFlow()

    private val _date = MutableStateFlow("")
    val date: StateFlow<String>
        get() = _date.asStateFlow()

    private val _score = MutableStateFlow("")
    val score: StateFlow<String>
        get() = _score.asStateFlow()

    private val _dateArr = MutableStateFlow<ArrayList<Int>>(ArrayList())
    val dateArr: StateFlow<ArrayList<Int>>
        get() = _dateArr.asStateFlow()

    private val _scoreOrDateEvents =
        MutableStateFlow<UiScoreOrDateEvent>(UiScoreOrDateEvent.Default)
    val scoreOrDateEvents: StateFlow<UiScoreOrDateEvent>
        get() = _scoreOrDateEvents.asStateFlow()

    private val _matchEvents = MutableStateFlow<UiMatchEvent>(UiMatchEvent.Loading)
    val matchEvents: StateFlow<UiMatchEvent>
        get() = _matchEvents.asStateFlow()

    private val _weatherEvents = MutableStateFlow<UiWeatherEvent>(UiWeatherEvent.EmptyState)
    val weatherEvents: StateFlow<UiWeatherEvent>
        get() = _weatherEvents.asStateFlow()

    private val timer = Timer()

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

    init {
        _matchId.value = bundle?.getInt("fixtureId").toString().toInt()
        getData()
    }

    private fun getData() {
        viewModelScope.launch(dispatchers) {
            matchRequest()
            weatherRequest()
        }
    }

    private suspend fun matchRequest() {
        val result = getMatchUsecase.execute(matchId.value)

        when (result) {
            ResultEvent.EmptyState -> {
                _matchEvents.value = UiMatchEvent.Loading
            }
            ResultEvent.Error -> {
                _matchEvents.value = UiMatchEvent.Error
            }
            is ResultEvent.Success -> {
                _city.value = result.value.fixture.venue.city
                _date.value = result.value.fixture.date
                //TODO Change set score
                _score.value =
                    result.value.goals.home.toString() + " : " + result.value.goals.away.toString()
                setScoreOrTime()
                _matchEvents.value = UiMatchEvent.Success(result.value)
            }
        }
    }

    private suspend fun weatherRequest() {
        val result = getWeatherUsecase.execute(city.value, date.value)
        when (result) {
            is ResultEvent.Success -> {
                _weatherEvents.value = UiWeatherEvent.Success(result.value)
            }
            is ResultEvent.EmptyState -> {
                _weatherEvents.value = UiWeatherEvent.EmptyState
            }
            is ResultEvent.Error -> _weatherEvents.value = UiWeatherEvent.Error
        }
    }

    private fun setScoreOrTime() {
        if (score.value == "? : ?") {
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        setTimeTillMatch()
                    }
                },
                0,
                MINUTE
            )
        } else {
            _scoreOrDateEvents.value = UiScoreOrDateEvent.HasScore
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setTimeTillMatch() {
        val dateFormat = SimpleDateFormat(DATE_PATTERN)
        val currentDateStr = sdf.format(Date())
        val currentDate = dateFormat.parse(currentDateStr)
        val matchDate = dateFormat.parse(date.value)

        val milliseconds: Long = matchDate.time - currentDate.time

        // 24 hours = 1 440 minutes = 1 day
        val days = (milliseconds / (24 * 60 * 60 * 1000)).toInt()
        //Log.d("MatchViewModel","Разница между датами в днях: $days")

        // 3 600 seconds = 60 minutes = 1 hour
        val hours = (milliseconds / (60 * 60 * 1000)).toInt() - (days * 24)

        // 60 000 millis = 60 seconds = 1 minute
        val minutes = ((milliseconds / (60 * 1000)).toInt() - (days * 24 * 60)) % 60

        _dateArr.value.add(minutes)
        _dateArr.value.add(hours)
        _dateArr.value.add(days)

        _scoreOrDateEvents.value = UiScoreOrDateEvent.HasDate
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.d("MatchViewModel", "On Cleared")
    }
}

sealed class UiScoreOrDateEvent {
    object HasScore : UiScoreOrDateEvent()
    object HasDate : UiScoreOrDateEvent()
    object Default : UiScoreOrDateEvent()
}

sealed class UiMatchEvent {
    class Success(val match: Match) : UiMatchEvent()
    object Loading : UiMatchEvent()
    object Error : UiMatchEvent()
}

sealed class UiWeatherEvent {
    class Success(val weather: MatchWeather) : UiWeatherEvent()
    object EmptyState : UiWeatherEvent()
    object Error : UiWeatherEvent()
}