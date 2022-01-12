package com.midina.stat_data.usecaseimpl

import com.midina.stat_data.LeagueStatisticsRepository
import com.midina.stat_domain.GetDataUsecase
import com.midina.stat_domain.model.ResultEvent
import com.midina.stat_domain.model.TopData
import io.reactivex.Single
import javax.inject.Inject

class GetDataUsecaseImpl @Inject constructor
    (private val leagueStatisticsRepository: LeagueStatisticsRepository) : GetDataUsecase {
    override fun execute(): Single<ResultEvent<Pair<Int, TopData>>> {
        return leagueStatisticsRepository.getData()
    }

}
