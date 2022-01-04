package com.midina.stat_ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.midina.stat_domain.GetDataUsecase
import com.midina.stat_domain.model.TopScorer
import com.midina.stat_domain.model.ResultEvent
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


    init {
        getData()
    }

    private fun getData() {

        getDataUsecase.execute().subscribe(object : SingleObserver<ResultEvent<Pair<Int,
                ResultEvent<TopScorer>>>> {
            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onSuccess(result: ResultEvent<Pair<Int, ResultEvent<TopScorer>>>) {
                Log.d(TAG, "onSuccess $result")
                when (result) {
                    is ResultEvent.Success<Pair<Int, ResultEvent<TopScorer>>> -> {
                        _seasonEvents.value = SeasonResultUiEvent.Success(result.value.first)
                        when (result.value.second) {
                            is ResultEvent.Success -> {
                                _topScorerEvents.value = TopScorerResultUiEvent.Success((result.value.second as ResultEvent.Success<TopScorer>).value)
                            }
                            is ResultEvent.EmptyState -> {
                                _topScorerEvents.value = TopScorerResultUiEvent.EmptyState
                            }
                        }
                    }
                    is ResultEvent.EmptyState -> {
                        _seasonEvents.value = SeasonResultUiEvent.EmptyState
                    }
                }
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onSuccess $e")
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