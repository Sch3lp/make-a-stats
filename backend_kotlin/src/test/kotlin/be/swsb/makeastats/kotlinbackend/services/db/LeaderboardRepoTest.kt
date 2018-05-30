package be.swsb.makeastats.kotlinbackend.services.db

import be.swsb.makeastats.kotlinbackend.model.Leaderboard
import be.swsb.makeastats.kotlinbackend.model.PlayerStats
import org.assertj.core.api.Assertions
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.testing.JdbiRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class LeaderboardRepoTest {

    @Rule @JvmField
    val db: JdbiRule = JdbiPreparedEmbeddedPostgres.preparedJdbi("db/migration").withPlugins();

    private var repo: LeaderboardRepo? = null

    @Before
    fun setUp() {
        repo = db.jdbi.onDemand()
    }

    @Test
    fun canSaveLeaderboard() {
        val leaderboard = Leaderboard(UUID.randomUUID(), "UvOiox", "ZF")
        val savedLeaderboard = repo?.insertAndFind(leaderboard)
        Assertions.assertThat(savedLeaderboard).isEqualTo(leaderboard)
    }

    @Test
    fun canFindLeaderboardByLeaderboardId() {
        val lid = "UvOiox"
        val leaderboard = Leaderboard(UUID.randomUUID(), lid, "ZF")
        repo?.insert(leaderboard)
        val savedLeaderboard = repo?.findByLeaderboardId(lid)
        Assertions.assertThat(savedLeaderboard).isEqualTo(leaderboard)
    }

    @Test
    fun cannotFindLeaderboardByLeaderboardId_ReturnsNull() {
        val lid = "UvOiox"
        val leaderboard = Leaderboard(UUID.randomUUID(), lid, "ZF")
        repo?.insert(leaderboard)
        val actual = repo?.findByLeaderboardId("snarfsnarf")
        Assertions.assertThat(actual).isNull()
    }
}