package com.midina.team_ui

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.fixtures.FixturesInfo
import com.midina.club_domain.model.team.TeamInfo
import com.midina.club_domain.usecases.GetFixturesUsecase
import com.midina.club_domain.usecases.GetSeasonUsecase
import com.midina.club_domain.usecases.GetTeamInfoUsecase
import com.midina.core_ui.ui.BaseFragment.Companion.FAVOURITE_TEAM_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

private const val DATE_PATTERN = "yyyy-MM-dd HH:mm"

class ClubViewModel @Inject constructor(
    private val getTeamInfoUsecase: GetTeamInfoUsecase,
    private val getSeasonUsecase: GetSeasonUsecase,
    private val getFixturesUsecase: GetFixturesUsecase,
    @Named("ClubBundle")
    bundle: Bundle?
) : ViewModel() {

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    private val _alarmEvents = MutableStateFlow<AlarmEvent>(AlarmEvent.Default)
    val alarmEvents: StateFlow<AlarmEvent>
        get() = _alarmEvents.asStateFlow()

    private val _teamId = MutableStateFlow(0)
    val teamId: StateFlow<Int>
        get() = _teamId.asStateFlow()

    private val _isAlarm = MutableStateFlow(false)
    val isAlarm: StateFlow<Boolean>
        get() = _isAlarm.asStateFlow()

    private val _season = MutableStateFlow(0)
    val season: StateFlow<Int>
        get() = _season.asStateFlow()

    init {
        if (bundle?.isEmpty == false) {
            _teamId.value = bundle.getInt(FAVOURITE_TEAM_ID)
        }
    }

    fun setTeamId(id: Int) {
        _teamId.value = id
    }

    fun dataRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            teamDataRequest()
        }
    }

    fun setIsAlarm(value: Boolean?) {
        if (value == null) {
            _isAlarm.value = !_isAlarm.value
        } else {
            _isAlarm.value = value
        }
    }

    fun alarmClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            seasonRequest()
        }
    }

    private suspend fun seasonRequest() {
        val result = getSeasonUsecase.execute()

        when (result) {
            ResultEvent.EmptyState -> _alarmEvents.value = AlarmEvent.EmptyState
            ResultEvent.Error -> _alarmEvents.value = AlarmEvent.Error
            is ResultEvent.Success -> {
                _season.value = result.value
                fixturesRequest()
            }
        }
    }

    private suspend fun fixturesRequest() {
        val result = getFixturesUsecase.execute(teamId.value, season.value)

        when (result) {
            ResultEvent.EmptyState -> _alarmEvents.value = AlarmEvent.EmptyState
            ResultEvent.Error -> _alarmEvents.value = AlarmEvent.Error
            is ResultEvent.Success -> _alarmEvents.value = AlarmEvent.Success(result.value)
        }
    }

    private suspend fun teamDataRequest() {
        val result = getTeamInfoUsecase.execute(teamId.value)

        when (result) {
            ResultEvent.EmptyState -> _events.value = UiEvent.EmptyState
            ResultEvent.Error -> _events.value = UiEvent.Error
            is ResultEvent.Success -> _events.value = UiEvent.Success(result.value)
        }
    }

    fun getTimeInMillis(matchTime: String): Long {
        return sdf.parse(matchTime).time
    }
}

sealed class UiEvent {
    class Success(val teamInfo: TeamInfo) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}

sealed class AlarmEvent {
    class Success(val list: ArrayList<FixturesInfo>) : AlarmEvent()
    object Error : AlarmEvent()
    object Default : AlarmEvent()
    object EmptyState : AlarmEvent()
}