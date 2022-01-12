package com.midina.stat_ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.midina.stat_domain.GetDataUsecase
import com.midina.stat_domain.model.*
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

const val TAG = "LeagueStatViewModel"

class LeagueStatViewModel @Inject constructor(private val getDataUsecase: GetDataUsecase) :
    ViewModel() {

    private val _seasonEvents =
        MutableStateFlow<SeasonResultUiEvent>(SeasonResultUiEvent.EmptyState)
    val seasonEvents: StateFlow<SeasonResultUiEvent>
        get() = _seasonEvents.asStateFlow()

    private val _topScorerEvents =
        MutableStateFlow<TopScorerResultUiEvent>(TopScorerResultUiEvent.EmptyState)
    val topScorerEvents: StateFlow<TopScorerResultUiEvent>
        get() = _topScorerEvents.asStateFlow()

    private val _topAssistantEvents =
        MutableStateFlow<TopAssistantResultUiEvent>(TopAssistantResultUiEvent.EmptyState)
    val topAssistantEvents: StateFlow<TopAssistantResultUiEvent>
        get() = _topAssistantEvents.asStateFlow()

    private val _topCleanSheetsEvents =
        MutableStateFlow<TopCleanSheetResultUiEvent>(TopCleanSheetResultUiEvent.EmptyState)
    val topCleanSheetsEvents: StateFlow<TopCleanSheetResultUiEvent>
        get() = _topCleanSheetsEvents.asStateFlow()


    private val _topTeamGoalsEvents =
        MutableStateFlow<TopTeamGoalsResultUiEvent>(TopTeamGoalsResultUiEvent.EmptyState)
    val topTeamGoalsEvents: StateFlow<TopTeamGoalsResultUiEvent>
        get() = _topTeamGoalsEvents.asStateFlow()

    init {
        getData()
    }

    private fun getData() {

        getDataUsecase.execute().subscribe(object : SingleObserver<ResultEvent<Pair<Int,
                TopData>>> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onSuccess(result: ResultEvent<Pair<Int, TopData>>) {
                Log.d(TAG, "onSuccess $result")

                when (result) {
                    is ResultEvent.Success<Pair<Int, TopData>> -> {
                        _seasonEvents.value = SeasonResultUiEvent.Success(result.value.first)

                        when (result.value.second.scorer) {
                            is ResultEvent.Success -> {
                                _topScorerEvents.value = TopScorerResultUiEvent
                                    .Success((result.value.second.scorer as ResultEvent.Success<TopScorer>).value)
                            }
                            is ResultEvent.EmptyState -> {
                                _topScorerEvents.value = TopScorerResultUiEvent.EmptyState
                            }
                        }

                        when (result.value.second.assistant) {
                            is ResultEvent.Success -> {
                                _topAssistantEvents.value = TopAssistantResultUiEvent
                                    .Success(
                                        (result.value.second.assistant
                                                as ResultEvent.Success<TopAssistant>).value
                                    )
                            }
                            is ResultEvent.EmptyState -> {
                                _topAssistantEvents.value = TopAssistantResultUiEvent.EmptyState
                            }
                        }

                        when (result.value.second.teamStat) {
                            is ResultEvent.Success -> {

                                val resultPair = result.value.second.teamStat

                                _topCleanSheetsEvents.value = (resultPair
                                        as ResultEvent.Success<Pair<TopCleanSheet?,
                                        TopTeamGoals?>>).value
                                    .first?.let {
                                        TopCleanSheetResultUiEvent
                                            .Success(it)
                                    }!!


                                _topTeamGoalsEvents.value = resultPair.value
                                    .second?.let {
                                        TopTeamGoalsResultUiEvent
                                            .Success(it)
                                    }!!

                            }
                            is ResultEvent.EmptyState -> {
                                _topCleanSheetsEvents.value = TopCleanSheetResultUiEvent.EmptyState
                                _topTeamGoalsEvents.value = TopTeamGoalsResultUiEvent.EmptyState
                            }
                            ResultEvent.Error -> TODO()
                        }
                    }
                    is ResultEvent.EmptyState -> {
                        _seasonEvents.value = SeasonResultUiEvent.EmptyState
                    }
                }
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError $e")
            }
        })
    }
}

sealed class SeasonResultUiEvent {
    class Success(val season: Int) : SeasonResultUiEvent()
    object Error : SeasonResultUiEvent()
    object EmptyState : SeasonResultUiEvent()
}

sealed class TopScorerResultUiEvent {
    class Success(val player: TopScorer) : TopScorerResultUiEvent()
    object Error : TopScorerResultUiEvent()
    object EmptyState : TopScorerResultUiEvent()
}

sealed class TopAssistantResultUiEvent {
    class Success(val player: TopAssistant) : TopAssistantResultUiEvent()
    object Error : TopAssistantResultUiEvent()
    object EmptyState : TopAssistantResultUiEvent()
}

sealed class TopCleanSheetResultUiEvent {
    class Success(val team: TopCleanSheet) : TopCleanSheetResultUiEvent()
    object Error : TopCleanSheetResultUiEvent()
    object EmptyState : TopCleanSheetResultUiEvent()
}

sealed class TopTeamGoalsResultUiEvent {
    class Success(val team: TopTeamGoals) : TopTeamGoalsResultUiEvent()
    object Error : TopTeamGoalsResultUiEvent()
    object EmptyState : TopTeamGoalsResultUiEvent()
}