package com.midina.stat_domain

import com.midina.stat_domain.model.ResultEvent
import com.midina.stat_domain.model.TopData
import com.midina.stat_domain.model.TopScorer
import io.reactivex.Single

interface GetDataUsecase {
    fun execute() : Single<ResultEvent<Pair<Int, TopData>>>
}