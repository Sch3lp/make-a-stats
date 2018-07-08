package be.swsb.makeastats.kotlinbackend.services

import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerName
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import be.swsb.makeastats.kotlinbackend.pubgacl.acl.PlayerAcl
import org.springframework.stereotype.Service

@Service
class PlayerStatsService(val playerStatsRepo: PlayerStatsRepo,
                         val playerAcl: PlayerAcl) {

    fun getByName(playerName: PlayerName): PlayerStats? {
        return playerStatsRepo.findByName(playerName)
    }

    fun createIfNotExistsByName(playerNames: Set<PlayerName>): List<PlayerStats> {
        val players = PlayerStats.fromPlayernames(playerNames)
                .map(playerStatsRepo::insertIfNotExistsByName)
        playerAcl.triggerPlayerUpdate(players.map { it.player })
        return players
    }

}


