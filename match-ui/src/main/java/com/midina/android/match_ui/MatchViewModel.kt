package com.midina.android.match_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val MINUTE = 60000L

@SuppressLint("SimpleDateFormat")
class MatchViewModel @Inject constructor(
    private val getWeatherUsecase: GetWeatherUsecase,
    bundle: Bundle?
) : ViewModel() {

    private val _dateArr = MutableStateFlow<ArrayList<Int>>(ArrayList())
    val dateArr: StateFlow<ArrayList<Int>>
        get() = _dateArr.asStateFlow()

    private val _scoreOrDateEvents = MutableStateFlow<UiScoreOrDateEvent>(UiScoreOrDateEvent.Default)
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

    private var lat = 0.0F
    private var lon = 0.0F

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
                lat = 49.84F
                lon = 24.02F
            }
            "Верес" -> {
                lat = 50.61F
                lon = 26.25F
            }
            "Шахтер Донецк" -> {
                lat = 50.44F
                lon = 30.52F
            }
            "Металлист 1925" -> {
                lat = 49.99F
                lon = 36.23F
            }
            "Десна" -> {
                lat = 51.49F
                lon = 31.28F
            }
            "Заря" -> {
                lat = 47.83F
                lon = 35.14F
            }
            "Ворскла" -> {
                lat = 49.58F
                lon = 34.55F
            }
            "Динамо Киев" -> {
                lat = 50.44F
                lon = 30.52F
            }
            "Мариуполь" -> {
                lat = 47.09F
                lon = 37.54F
            }
            "Колос К" -> {
                lat = 49.98F
                lon = 30.01F
            }
            "Ингулец" -> {
                lat = 48.34F
                lon = 33.26F
            }
            "Рух Львов" -> {
                lat = 49.84F
                lon = 24.02F
            }
            "Черноморец" -> {
                lat = 46.47F
                lon = 30.73F
            }
            "Александрия" -> {
                lat = 48.67F
                lon = 33.10F
            }
            "Днепр-1" -> {
                lat = 48.46F
                lon = 35.04F
            }
            "Минай" -> {
                lat = 48.59F
                lon = 22.28F
            }
        }

    }

    private fun getData() {
        getCoordinates()
        viewModelScope.launch(Dispatchers.IO) {
            val result = getWeatherUsecase.execute(lat, lon, date.value)
            when (result) {
                is ResultEvent.Success -> {
                    _events.value = UiEvent.Success(result.value)
                }
                is ResultEvent.EmptyState -> {
                    _events.value = UiEvent.EmptyState
                }
                is ResultEvent.Error -> _events.value = UiEvent.Error
            }
            Log.d("VM", "RESULT")
        }
    }

    private fun getScoreOrTime() {
        if (score.value == "? : ?") {
            timer.scheduleAtFixedRate(
                object  : TimerTask() {
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

        // 24 часа = 1 440 минут = 1 день
        val days = (milliseconds / (24 * 60 * 60 * 1000)).toInt()
        //Log.d("MatchViewModel","Разница между датами в днях: $days")

        // 3 600 секунд = 60 минут = 1 час
        val hours = (milliseconds / (60 * 60 * 1000)).toInt() - (days * 24)
        // Log.d("MatchViewModel","Разница между датами в часах: $hours")

        // 60 000 миллисекунд = 60 секунд = 1 минута
        val minutes = ((milliseconds / (60 * 1000)).toInt() - (days * 24 * 60)) % 60
        Log.d("MatchViewModel", "Разница между датами в минутах: $minutes")

        _dateArr.value.add(minutes)
        _dateArr.value.add(hours)
        _dateArr.value.add(days)

        Log.d("MatchViewModel", "Date Arr Check ${_dateArr.value}")
        _scoreOrDateEvents.value = UiScoreOrDateEvent.HasDate
    }

    fun getHomeTeamName(): String {
        when (homeTeam.value) {
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

    fun getGuestTeamName(): String {
        when (guestTeam.value) {
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