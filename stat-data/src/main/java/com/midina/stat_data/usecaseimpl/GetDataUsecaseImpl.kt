package com.midina.stat_data.usecaseimpl

import com.midina.stat_data.LeagueStatisticsRepository
import com.midina.stat_domain.GetAsyncUsecase
import com.midina.stat_domain.GetDataUsecase
import com.midina.stat_domain.model.AsyncTopData
import com.midina.stat_domain.model.ResultEvent
import com.midina.stat_domain.model.TopData
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class GetDataUsecaseImpl @Inject constructor
    (private val leagueStatisticsRepository: LeagueStatisticsRepository) : GetDataUsecase {
    override fun execute(): Single<ResultEvent<Pair<Int, TopData>>> {
        return leagueStatisticsRepository.getData()
    }

}

class GetAsyncDataUsecaseImpl @Inject constructor
    (private val leagueStatisticsRepository: LeagueStatisticsRepository) : GetAsyncUsecase {
    override fun execute(): PublishSubject<ResultEvent<AsyncTopData>> {
        return leagueStatisticsRepository.getAsyncData()
    }
}

