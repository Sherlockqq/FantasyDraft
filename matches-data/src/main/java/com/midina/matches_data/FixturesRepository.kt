package com.midina.matches_data

import com.midina.matches_data.api.FixturesApiInterface
import com.midina.matches_data.data.fixtures.FixturesData
import com.midina.matches_data.data.tour.CurrentTourData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FixturesRepository @Inject constructor(private val fixturesApi: FixturesApiInterface) {

    suspend fun getMatchMap(season: Int): FixturesData {
        return fixturesApi.getFixtures(season = season)
    }

    suspend fun getTour(season: Int): CurrentTourData {
        return fixturesApi.getCurrentTour(season = season)
    }
}
