package com.midina.club_data

import com.midina.club_data.api.ClubApiInterface
import com.midina.club_data.data.fixturesdata.FixturesData
import com.midina.club_data.data.leaguepref.Fixtures
import com.midina.club_data.data.leaguepref.LeaguePreferences
import com.midina.club_data.data.teaminfodata.TeamInfoData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClubRepository @Inject constructor(private val clubApiInterface: ClubApiInterface) {

    suspend fun getTeamInfoData(teamId: Int): TeamInfoData =
        clubApiInterface.getTeamInfoData(teamId)

    suspend fun getLeaguePreferences(): LeaguePreferences =
        clubApiInterface.getLeaguePreferences()

    suspend fun getFixtures(teamId: Int, season: Int): FixturesData =
        clubApiInterface.getFixtures(teamId, season)
}