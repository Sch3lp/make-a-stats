package be.swsb.makeastats.kotlinbackend.services

import be.swsb.makeastats.kotlinbackend.domain.leaderboard.*
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import org.springframework.stereotype.Service

@Service
class LeaderboardService(val leaderboardRepo: LeaderboardRepo,
                         val playerStatsRepo: PlayerStatsRepo) {

    fun handle(cmd: CreateLeaderBoardCmd): LeaderboardWithPlayers? {
        val playerIds = PlayerStats.fromPlayernames(cmd.playerNames)
                .map(playerStatsRepo::insertIfNotExistsByName)
                .map(PlayerStats::id)
        val leaderboard = Leaderboard(cmd)
        val persistedLeaderboard = leaderboardRepo.insertAndFind(leaderboard)
        leaderboardRepo.addPlayersToLeaderboard(persistedLeaderboard.lid, playerIds)
        return findLeaderboardWithPlayers(persistedLeaderboard.lid)
    }

    fun findLeaderboardWithPlayers(lid: LeaderboardHashId): LeaderboardWithPlayers? {
        return leaderboardRepo.findByLeaderboardId(lid)?.let({
            LeaderboardWithPlayers(it.id,
                    it.lid,
                    it.name,
                    leaderboardRepo.findLeaderboardWithPlayersByLeaderboardId(lid).map {
                        playerStatsRepo.findById(it.playerId)
                    }
            )
        })
    }
}