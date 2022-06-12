package com.midina.club_data.usecaseimpl

import com.midina.club_data.ClubRepository
import com.midina.club_data.data.playersdata.PlayersData
import com.midina.club_domain.model.ResultEvent
import com.midina.club_domain.model.player.Player
import com.midina.club_domain.usecases.GetPlayersUsecase
import javax.inject.Inject


class GetPlayersUsecaseImpl @Inject constructor(private val clubRepository: ClubRepository) :
    GetPlayersUsecase {
    override suspend fun execute(teamId: Int, season: Int): ResultEvent<List<Player>> {

        return try {
            val playersData = clubRepository.getPlayers(teamId, season)

            if (playersData.response.isEmpty()) {
                ResultEvent.EmptyState
            } else {
                ResultEvent.Success(playersData.toListOfPlayer())
            }

        } catch (e: Exception) {
            ResultEvent.Error
        }
    }

    private fun PlayersData.toListOfPlayer(): List<Player> {
        val list = mutableListOf<Player>()

        this.response.forEach { response ->
            list.add(
                Player(
                    id = response.player.id,
                    name = response.player.name,
                    nationality = response.player.nationality,
                    position = response.statistics[0].games.position,
                    photo = response.player.photo,
                    isHeader = false
                )
            )
        }
        return list.sort()
    }

    private fun List<Player>.sort(): List<Player> {
        val goalkeeperList = this.getByPosition("Goalkeeper")
        val defenderList = this.getByPosition("Defender")
        val midfielderList = this.getByPosition("Midfielder")
        val attackerList = this.getByPosition("Attacker")

        return goalkeeperList + defenderList + midfielderList + attackerList
    }

    private fun List<Player>.getByPosition(position: String): List<Player> {
        val list = mutableListOf<Player>()

        list.add(Player(0, "", "", position, "", true))

        return list + this.filter { it.position == position}
    }
}