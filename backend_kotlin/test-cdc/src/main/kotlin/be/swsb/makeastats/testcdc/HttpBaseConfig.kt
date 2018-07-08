package be.swsb.makeastats.testcdc

import be.swsb.makeastats.kotlinbackend.domain.leaderboard.*
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import be.swsb.makeastats.kotlinbackend.services.LeaderboardService
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
        return LeaderboardServiceStub(leaderboardRepo(), playerStatsRepo())
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
    open fun datasource(): DataSource {
        return mock()
    }
}

class LeaderboardServiceStub(override val leaderboardRepo: LeaderboardRepo,
                             override val playerStatsRepo: PlayerStatsRepo)
    : LeaderboardService(leaderboardRepo, playerStatsRepo) {

    override fun handle(cmd: CreateLeaderBoardCmd): LeaderboardWithPlayers? {
        val leaderboard = Leaderboard(CreateLeaderBoardCmd("shroudSquad", setOf("shrood", "wadu")))
        return LeaderboardWithPlayers(leaderboard.id, leaderboard.lid, leaderboard.name, PlayerStats.fromPlayernames(setOf("shrood", "wadu")))
    }

    override fun findLeaderboardWithPlayers(lid: LeaderboardHashId): LeaderboardWithPlayers? {
        val leaderboard = Leaderboard(CreateLeaderBoardCmd("shroudSquad", setOf("shrood","wadu")))
        return LeaderboardWithPlayers(leaderboard.id, leaderboard.lid, leaderboard.name, PlayerStats.fromPlayernames(setOf("shrood", "wadu")))
    }
}
