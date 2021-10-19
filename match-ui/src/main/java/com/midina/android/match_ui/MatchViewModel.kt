package com.midina.android.match_ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import com.midina.core_ui.ui.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
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
) : ViewModel(), LifecycleObserver {

    private val _dateArr = MutableLiveData<ArrayList<Int>>()
    val dateArr: LiveData<ArrayList<Int>>
        get() = _dateArr

    private val _scoreOrDateEvents = SingleLiveEvent<UiScoreOrDateEvent>()
    val scoreOrDateEvents: LiveData<UiScoreOrDateEvent>
        get() = _scoreOrDateEvents

    private val _events = SingleLiveEvent<UiEvent>()
    val events: LiveData<UiEvent>
        get() = _events

    var homeTeam: String = ""
    var guestTeam: String = ""
    var score: String = ""
    var date: String = ""

    private var lat = 0.0F
    private var lon = 0.0F

    private val timer = Timer()

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

    init {
        _dateArr.value = ArrayList()
        homeTeam = bundle?.getString("HomeTeam").toString()
        guestTeam = bundle?.getString("GuestTeam").toString()
        score = bundle?.getString("Score").toString()
        date = bundle?.getString("Date").toString()
        getScoreOrTime()
    }


    private fun getCoordinates() {
        when (homeTeam) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun getData() {
        getCoordinates()
        viewModelScope.launch(Dispatchers.IO) {
            val result = getWeatherUsecase.execute(lat, lon, date)
            when (result) {
                is ResultEvent.Success -> {
                    _events.postValue(UiEvent.Success(result.value))
                }
                is ResultEvent.EmptyState -> {
                    _events.postValue(UiEvent.EmptyState)
                }
                is ResultEvent.Error -> _events.postValue(UiEvent.Error)
            }
            Log.d("VM", "RESULT")
        }
    }

    private fun getScoreOrTime() {
        if (score == "? : ?") {
            timer.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        _dateArr.postValue(getTimeTillMatch())
                        Log.d("MatchViewModel", "Date Arr Check ${_dateArr.value}")
                        _scoreOrDateEvents.postValue(UiScoreOrDateEvent.HasDate)
                    }
                },
                0,
                MINUTE
            )
        } else {
            _scoreOrDateEvents.postValue(UiScoreOrDateEvent.HasScore)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTimeTillMatch(): ArrayList<Int> {
        val dateFormat = SimpleDateFormat(DATE_PATTERN)
        val currentDateStr = sdf.format(Date())
        val currentDate = dateFormat.parse(currentDateStr)
        val matchDate = dateFormat.parse(date)

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

        val date = ArrayList<Int>()

        date.add(minutes)
        date.add(hours)
        date.add(days)

        return date
    }

    fun getHomeTeamName(): String {
        when (homeTeam) {
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
        when (guestTeam) {
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
    }
}

sealed class UiScoreOrDateEvent {
    object HasScore : UiScoreOrDateEvent()
    object HasDate : UiScoreOrDateEvent()
}

sealed class UiEvent {
    class Success(val weather: MatchWeather) : UiEvent()
    object EmptyState : UiEvent()
    object Error : UiEvent()
}