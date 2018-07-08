package be.swsb.makeastats.kotlinbackend.services

import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerName
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerStatsService(val playerStatsRepo: PlayerStatsRepo) {

    fun getByName(playerName: String): PlayerStats? {
        return playerStatsRepo.findByName(playerName)
    }

    fun createIfNotExistsByName(playerNames: Set<String>): List<PlayerStats> {
        return PlayerStats.fromPlayernames(playerNames)
                .map(playerStatsRepo::insertIfNotExistsByName)
    }

}


