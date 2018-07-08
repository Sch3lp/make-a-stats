package be.swsb.makeastats.kotlinbackend.services

import be.swsb.makeastats.kotlinbackend.domain.leaderboard.CreateLeaderBoardCmd
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.leaderboard.LeaderboardRepo
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import be.swsb.makeastats.kotlinbackend.test.JdbiPreparedEmbeddedPostgresKotlin
import org.assertj.core.api.Assertions
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.testing.JdbiRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeaderboardServiceTest {

    @Rule @JvmField
    val db: JdbiRule = JdbiPreparedEmbeddedPostgresKotlin.preparedJdbi().withPlugins()

    private lateinit var leaderboardRepo: LeaderboardRepo
    private lateinit var playerStatsRepo: PlayerStatsRepo
    private lateinit var leaderboardService: LeaderboardService

    @Before
    fun setUp() {
        leaderboardRepo = db.jdbi.onDemand()
        playerStatsRepo = db.jdbi.onDemand()
        leaderboardService = LeaderboardService(leaderboardRepo, playerStatsRepo)
    }

    @Test
    fun handleLeaderboardCreation_CreatesLeaderboard_CreatesPlayers_StartsAsyncPubgCalls() {
        val leaderboard = leaderboardService.handle(CreateLeaderBoardCmd("ZF", setOf("womble", "cyanide")))!!

        val foundLeaderboard = leaderboardRepo.findByLeaderboardId(leaderboard.lid)

        Assertions.assertThat(foundLeaderboard?.lid).isEqualTo(leaderboard.lid)
        Assertions.assertThat(foundLeaderboard?.name).isEqualTo("ZF")
        Assertions.assertThat(playerStatsRepo.list().map(PlayerStats::player)).containsOnly("womble","cyanide")
        //TODO verify pubg calls are made
    }

    @Test
    fun handleLeaderboardCreation_PlayersAlreadyExist_DoesNotRecreatePlayers() {
        val leaderboard = leaderboardService.handle(CreateLeaderBoardCmd("ZF", setOf("womble", "cyanide")))!!
        val leaderboard2 = leaderboardService.handle(CreateLeaderBoardCmd("ZFShroud", setOf("womble", "cyanide", "shroud", "chad")))!!

        val foundLeaderboard = leaderboardRepo.findByLeaderboardId(leaderboard.lid)
        val foundLeaderboard2 = leaderboardRepo.findByLeaderboardId(leaderboard2.lid)
        Assertions.assertThat(foundLeaderboard?.name).isEqualTo("ZF")
        Assertions.assertThat(foundLeaderboard2?.name).isEqualTo("ZFShroud")
        Assertions.assertThat(playerStatsRepo.list().map(PlayerStats::player)).containsExactlyInAnyOrder("womble","cyanide","shroud","chad")
    }
}