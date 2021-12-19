package com.midina.stat_ui

import androidx.lifecycle.ViewModel
import com.midina.stat_domain.GetSeasonUseCase
import javax.inject.Inject

class LeagueStatViewModel @Inject constructor(private val getSeasonUsecase: GetSeasonUseCase) :
    ViewModel() {

    init {
        getSeason()
    }


    private fun getSeason() {
        getSeasonUsecase.execute()
    }
}