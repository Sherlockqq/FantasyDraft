package com.midina.club_data.usecaseimpl

import com.midina.club_data.ClubRepository
import com.midina.club_data.data.teaminfodata.TeamInfoData
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.team.Stadium
import com.midina.club_domain.model.team.Team
import com.midina.club_domain.model.team.TeamInfo
import com.midina.club_domain.usecases.GetTeamInfoUsecase
import javax.inject.Inject

class GetTeamInfoUsecaseImpl @Inject constructor(private val clubRepository: ClubRepository) :
    GetTeamInfoUsecase {
    override suspend fun execute(teamId: Int): ResultEvent<TeamInfo> {
        return try {
            val teamInfoData = clubRepository.getTeamInfoData(teamId)

            if (teamInfoData.results == 0) {
                ResultEvent.EmptyState
            } else {
                ResultEvent.Success(teamInfoData.toTeamInfo())
            }
        } catch (e: Exception) {
            ResultEvent.Error
        }
    }

    private fun TeamInfoData.toTeamInfo(): TeamInfo {
        return TeamInfo(
            toTeam(),
            toStadium()
        )
    }

    private fun TeamInfoData.toTeam(): Team {
        return Team(
            name = response[0].team.name,
            logo = response[0].team.logo
        )
    }

    private fun TeamInfoData.toStadium(): Stadium {
        return Stadium(
            name = response[0].venue.name,
            logo = response[0].venue.image
        )
    }

}