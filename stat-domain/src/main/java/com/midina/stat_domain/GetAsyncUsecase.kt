package com.midina.stat_domain

import com.midina.stat_domain.model.AsyncTopData
import com.midina.stat_domain.model.ResultEvent
import io.reactivex.subjects.PublishSubject

interface GetAsyncUsecase {
    fun execute() : PublishSubject<ResultEvent<AsyncTopData>>
}