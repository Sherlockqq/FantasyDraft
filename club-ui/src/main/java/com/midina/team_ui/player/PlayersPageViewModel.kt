package com.midina.team_ui.player

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.player.Player
import com.midina.club_domain.usecases.GetPlayersUsecase
import com.midina.core_domain.usecases.GetSeasonUsecase
import com.midina.core_ui.ui.BaseFragment.Companion.SEASON
import com.midina.core_ui.ui.BaseFragment.Companion.TEAM_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class PlayersPageViewModel @Inject constructor(
    private val getPlayersUsecase: GetPlayersUsecase,
    private val getSeasonUsecase: GetSeasonUsecase,
    @Named("PlayerBundle")
    bundle: Bundle?
) : ViewModel() {

    private val _playersEvents = MutableStateFlow<PlayerUiEvent>(PlayerUiEvent.Loading)
    val playersEvents: StateFlow<PlayerUiEvent>
        get() = _playersEvents.asStateFlow()

    private val _teamId = MutableStateFlow(0)
    val teamId: StateFlow<Int>
        get() = _teamId.asStateFlow()

    private val _season = MutableStateFlow(0)
    val season: StateFlow<Int>
        get() = _season.asStateFlow()

    init {
        if (bundle?.isEmpty == false) {
            _teamId.value = bundle.getInt(TEAM_ID)
        }
        viewModelScope.launch(Dispatchers.IO) {
            seasonRequest()
        }
    }

    private suspend fun playerRequest() {
        val result = getPlayersUsecase.execute(teamId.value, season.value)

        when(result) {
            ResultEvent.EmptyState -> _playersEvents.value = PlayerUiEvent.EmptyState
            ResultEvent.Error -> _playersEvents.value = PlayerUiEvent.Error
            is ResultEvent.Success -> _playersEvents.value = PlayerUiEvent.Success(result.value)
        }
    }
    private suspend fun seasonRequest() {
        val result = getSeasonUsecase.execute()

        when(result) {
            com.midina.core_domain.model.ResultEvent.EmptyState -> _playersEvents.value = PlayerUiEvent.EmptyState
            com.midina.core_domain.model.ResultEvent.Error -> _playersEvents.value = PlayerUiEvent.Error
            is com.midina.core_domain.model.ResultEvent.Success -> {
                _season.value = result.value
                playerRequest()
            }
        }

    }
}

sealed class PlayerUiEvent {
    class Success(val players: List<Player>) : PlayerUiEvent()
    object Error : PlayerUiEvent()
    object Loading : PlayerUiEvent()
    object EmptyState : PlayerUiEvent()
}