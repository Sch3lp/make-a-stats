package be.swsb.makeastats.testcdc

import be.swsb.makeastats.kotlinbackend.domain.leaderboard.*
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import be.swsb.makeastats.kotlinbackend.pubgacl.acl.PlayerAcl
import be.swsb.makeastats.kotlinbackend.services.LeaderboardService
import be.swsb.makeastats.kotlinbackend.services.PlayerStatsService
import com.nhaarman.mockito_kotlin.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
open class HttpBaseConfig {

    @Bean
    @Primary
    open fun leaderboardService(): LeaderboardService {
        return LeaderboardServiceStub(leaderboardRepo(), playerStatsRepo(), playerStatsService())
    }

    @Bean
    @Primary
    open fun playerStatsRepo(): PlayerStatsRepo {
        return mock()
    }

    @Bean
    @Primary
    open fun leaderboardRepo(): LeaderboardRepo {
        return mock()
    }

    @Bean
    @Primary
    open fun playerAcl(): PlayerAcl {
        return mock()
    }

    @Bean
    @Primary
    open fun playerStatsService(): PlayerStatsService {
        return mock()
    }

    @Bean
    @Primary
    open fun datasource(): DataSource {
        return mock()
    }
}

class LeaderboardServiceStub(override val leaderboardRepo: LeaderboardRepo,
                             override val playerStatsRepo: PlayerStatsRepo,
                             override val playerStatsService: PlayerStatsService)
    : LeaderboardService(leaderboardRepo, playerStatsRepo, playerStatsService) {

    override fun handle(cmd: CreateLeaderBoardCmd): LeaderboardWithPlayers? {
        val leaderboard = Leaderboard(CreateLeaderBoardCmd("shroudSquad", setOf("shrood", "wadu")))
        return LeaderboardWithPlayers(leaderboard.id, leaderboard.lid, leaderboard.name, PlayerStats.fromPlayernames(setOf("shrood", "wadu")))
    }

    override fun findLeaderboardWithPlayers(lid: LeaderboardHashId): LeaderboardWithPlayers? {
        val leaderboard = Leaderboard(CreateLeaderBoardCmd("shroudSquad", setOf("shrood","wadu")))
        return LeaderboardWithPlayers(leaderboard.id, leaderboard.lid, leaderboard.name, PlayerStats.fromPlayernames(setOf("shrood", "wadu")))
    }
}
