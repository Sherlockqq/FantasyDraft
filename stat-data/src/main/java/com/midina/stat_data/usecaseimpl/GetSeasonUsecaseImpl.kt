package com.midina.stat_data.usecaseimpl

import com.midina.stat_data.LeagueStatisticsRepository
import com.midina.stat_domain.GetSeasonUseCase
import javax.inject.Inject

class GetSeasonUsecaseImpl @Inject constructor
    (private val leagueStatisticsRepository: LeagueStatisticsRepository) : GetSeasonUseCase {
    override fun execute() {
        leagueStatisticsRepository.getSeason().subscribe()
    }
}