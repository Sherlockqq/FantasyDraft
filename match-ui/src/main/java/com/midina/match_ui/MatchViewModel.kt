package com.midina.match_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val MINUTE = 60000L

@SuppressLint("SimpleDateFormat")
class MatchViewModel @Inject constructor(
    private val getWeatherUsecase: GetWeatherUsecase,
    private val dispatchers: CoroutineDispatcher,
    @Named("MatchBundle")
    bundle: Bundle?
) : ViewModel() {

    private val _dateArr = MutableStateFlow<ArrayList<Int>>(ArrayList())
    val dateArr: StateFlow<ArrayList<Int>>
        get() = _dateArr.asStateFlow()

    private val _scoreOrDateEvents =
        MutableStateFlow<UiScoreOrDateEvent>(UiScoreOrDateEvent.Default)
    val scoreOrDateEvents: StateFlow<UiScoreOrDateEvent>
        get() = _scoreOrDateEvents.asStateFlow()

    private val _events = MutableStateFlow<UiEvent>(UiEvent.EmptyState)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    private val _homeTeam = MutableStateFlow("")
    val homeTeam: StateFlow<String>
        get() = _homeTeam.asStateFlow()

    private val _guestTeam = MutableStateFlow("")
    val guestTeam: StateFlow<String>
        get() = _guestTeam.asStateFlow()

    private val _score = MutableStateFlow("")
    val score: StateFlow<String>
        get() = _score.asStateFlow()

    private val _date = MutableStateFlow("")
    val date: StateFlow<String>
        get() = _date.asStateFlow()

    private val _lat = MutableStateFlow(0.0F)
    val lat: StateFlow<Float>
        get() = _lat.asStateFlow()

    private val _lon = MutableStateFlow(0.0F)
    val lon: StateFlow<Float>
        get() = _lon.asStateFlow()

    private val timer = Timer()

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

    init {
        _homeTeam.value = bundle?.getString("HomeTeam").toString()
        _guestTeam.value = bundle?.getString("GuestTeam").toString()
        _score.value = bundle?.getString("Score").toString()
        _date.value = bundle?.getString("Date").toString()
        getData()
        getScoreOrTime()
    }

    private fun getCoordinates() {
        when (_homeTeam.value) {
            "Львов" -> {
                _lat.value = 49.84F
                _lon.value = 24.02F
            }
            "Верес" -> {
                _lat.value = 50.61F
                _lon.value = 26.25F
            }
            "Шахтер Донецк" -> {
                _lat.value = 50.44F
                _lon.value = 30.52F
            }
            "Металлист 1925" -> {
                _lat.value = 49.99F
                _lon.value = 36.23F
            }
            "Десна" -> {
                _lat.value = 51.49F
                _lon.value = 31.28F
            }
            "Заря" -> {
                _lat.value = 47.83F
                _lon.value = 35.14F
            }
            "Ворскла" -> {
                _lat.value = 49.58F
                _lon.value = 34.55F
            }
            "Динамо Киев" -> {
                _lat.value = 50.44F
                _lon.value = 30.52F
            }
            "Мариуполь" -> {
                _lat.value = 47.09F
                _lon.value = 37.54F
            }
            "Колос К" -> {
                _lat.value = 49.98F
                _lon.value = 30.01F
            }
            "Ингулец" -> {
                _lat.value = 48.34F
                _lon.value = 33.26F
            }
            "Рух Львов" -> {
                _lat.value = 49.84F
                _lon.value = 24.02F
            }
            "Черноморец" -> {
                _lat.value = 46.47F
                _lon.value = 30.73F
            }
            "Александрия" -> {
                _lat.value = 48.67F
                _lon.value = 33.10F
            }
            "Днепр-1" -> {
                _lat.value = 48.46F
                _lon.value = 35.04F
            }
            "Минай" -> {
                _lat.value = 48.59F
                _lon.value = 22.28F
            }
        }
    }

    private fun getData() {
        getCoordinates()
        viewModelScope.launch(dispatchers) {
            getWeather()
        }
    }

    private suspend fun getWeather() {
        val result = getWeatherUsecase.execute(_lat.value, _lon.value, date.value)
        when (result) {
            is ResultEvent.Success -> {
                _events.value = UiEvent.Success(result.value)
            }
            is ResultEvent.EmptyState -> {
                _events.value = UiEvent.EmptyState
            }
            is ResultEvent.Error -> _events.value = UiEvent.Error
        }
    }

    private fun getScoreOrTime() {
        if (score.value == "? : ?") {
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        getTimeTillMatch()
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
    private fun getTimeTillMatch() {
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

    fun getTeamName(teamName: String): String {
        when (teamName) {
            "Львов" -> return "ЛЬВ"
            "Верес" -> return "ВЕР"
            "Шахтер Донецк" -> return "ШАХ"
            "Металлист 1925" -> return "МЕТ"
            "Десна" -> return "ДЕС"
            "Заря" -> return "ЗАР"
            "Ворскла" -> return "ВОР"
            "Динамо Киев" -> return "ДИН"
            "Мариуполь" -> return "МАР"
            "Колос К" -> return "КОЛ"
            "Ингулец" -> return "ИНГ"
            "Рух Львов" -> return "РУХ"
            "Черноморец" -> return "ЧЕР"
            "Александрия" -> return "АЛЕ"
            "Днепр-1" -> return "ДНЕ"
            "Минай" -> return "МИН"
        }
        return "КОМ"
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

sealed class UiEvent {
    class Success(val weather: MatchWeather) : UiEvent()
    object EmptyState : UiEvent()
    object Error : UiEvent()
}