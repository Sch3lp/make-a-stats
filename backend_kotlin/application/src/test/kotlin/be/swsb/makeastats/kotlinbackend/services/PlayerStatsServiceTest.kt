package be.swsb.makeastats.kotlinbackend.services

import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStats
import be.swsb.makeastats.kotlinbackend.domain.playerstats.PlayerStatsRepo
import be.swsb.makeastats.kotlinbackend.pubgacl.acl.PlayerAcl
import be.swsb.makeastats.kotlinbackend.test.JdbiPreparedEmbeddedPostgresKotlin
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.jdbi.v3.testing.JdbiRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayerStatsServiceTest {

    @Rule
    @JvmField
    val db: JdbiRule = JdbiPreparedEmbeddedPostgresKotlin.preparedJdbi().withPlugins()

    private val playerAcl: PlayerAcl = mock()
    private lateinit var playerStatsService: PlayerStatsService
    private lateinit var playerStatsRepo: PlayerStatsRepo

    @Before
    fun setUp() {
        playerStatsRepo = db.jdbi.onDemand()

        playerStatsService = PlayerStatsService(playerStatsRepo, playerAcl)
    }

    @Test
    fun createIfNotExistsByName_CreatesPlayers_StartsAsyncPubgCalls() {
        playerStatsService.createIfNotExistsByName(setOf("shrood", "chad"))

        Assertions.assertThat(playerStatsRepo.list().map(PlayerStats::player)).containsOnly("shrood", "chad")

        verify(playerAcl).triggerPlayerUpdate(listOf("shrood", "chad"))
    }
}