package com.midina.splash_data.usecaseimpl

import com.midina.splash_data.SplashRepository
import com.midina.splash_domain.model.ResultEvent
import com.midina.splash_domain.usecase.GetSeasonUsecase
import javax.inject.Inject

class GetSeasonUsecaseImpl @Inject constructor
    (private val splashRepository: SplashRepository) : GetSeasonUsecase {
    override suspend fun execute(): ResultEvent<Int> = splashRepository.getCurrentSeason()
}