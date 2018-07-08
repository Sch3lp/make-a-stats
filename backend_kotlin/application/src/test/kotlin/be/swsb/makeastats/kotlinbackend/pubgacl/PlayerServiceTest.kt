package be.swsb.makeastats.kotlinbackend.pubgacl

import be.swsb.makeastats.kotlinbackend.objectMapper
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PlayerService
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PubgApiConfig
import org.assertj.core.api.Assertions
import org.junit.Test

class PlayerServiceTest {

    val playerService: PlayerService = PlayerService(PubgApiConfig("http://localhost:3333/pubg-stub", ""), objectMapper())

    @Test
    fun canCallAndMap() {
        val model = playerService.findPlayersByNames(listOf("shroud", "chad"))
                .test()
                .apply { awaitTerminalEvent() }
                .assertNoErrors()
                .assertValueCount(1)
                .assertComplete()
                .values()[0]

        Assertions.assertThat(model).isNotNull()
    }
}