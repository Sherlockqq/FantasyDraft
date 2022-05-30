package com.midina.club_data.usecaseimpl

import com.midina.club_data.ClubRepository
import com.midina.club_data.data.fixturesdata.FixturesData
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.fixtures.FixturesInfo
import com.midina.club_domain.usecases.GetFixturesUsecase
import java.lang.StringBuilder
import javax.inject.Inject

class GetFixturesUsecaseImpl @Inject constructor(private val clubRepository: ClubRepository) :
    GetFixturesUsecase {
    override suspend fun execute(teamId: Int, season: Int): ResultEvent<ArrayList<FixturesInfo>> {

        return try {
            val fixturesData = clubRepository.getFixtures(teamId, season)

            if (fixturesData.results == 0) {
                ResultEvent.EmptyState
            } else {
                ResultEvent.Success(fixturesData.toListFixturesInfo())
            }
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }


    private fun FixturesData.toListFixturesInfo(): ArrayList<FixturesInfo> {
        val array = arrayListOf<FixturesInfo>()

        response.forEach {
            array.add(
                FixturesInfo(
                    tour = it.league.round,
                    home = it.teams.home.name,
                    away = it.teams.away.name,
                    date = getDateByPattern(it.fixture.date)
                )
            )
        }
        return array
    }

    private fun getDateByPattern(date: String): String {
        val str = StringBuilder(date.substring(0, date.length - 9))
        str[10] = ' '
        return str.toString()
    }
}