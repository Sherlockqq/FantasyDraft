package com.midina.android.match_ui

import android.util.Log
import androidx.lifecycle.*
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeather
import com.midina.core_ui.ui.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MatchViewModel @Inject constructor(
    val getWeather: GetWeather,
    val homeTeam: String
) : ViewModel(), LifecycleObserver {
//todo constctour Factory
//todo dagger

    init {
        Log.d("TODO Remove", "injected homeTeam : $homeTeam")
    }
    private val _events = SingleLiveEvent<UiEvent>()
    val events: LiveData<UiEvent>
        get() = _events

   // var homeTeam: String = ""
    var guestTeam: String = ""
    var score: String = ""
    var date: String = ""

    private var lat = 0.0F
    private var lon = 0.0F

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
            val result = getWeather.execute(lat, lon, date)
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
}

sealed class UiEvent {
    class Success(val weather: MatchWeather) : UiEvent()
    object EmptyState : UiEvent()
    object Error : UiEvent()
}