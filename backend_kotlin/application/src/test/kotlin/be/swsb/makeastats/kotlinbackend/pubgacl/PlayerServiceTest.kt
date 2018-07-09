package be.swsb.makeastats.kotlinbackend.pubgacl

import be.swsb.makeastats.kotlinbackend.objectMapper
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PlayerService
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PubgApiConfig
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model.PubgApiWrapper
import com.github.kittinunf.result.Result
import org.assertj.core.api.Assertions
import org.junit.Test

class PlayerServiceTest {

    val playerService: PlayerService = PlayerService(PubgApiConfig("http://localhost:3333/pubg-stub", ""), objectMapper())

    @Test
    fun canCallAndMap() {
        playerService.findPlayersByNames(listOf("shroud", "chad")) { req, res, result ->
            val model: Any = when(result) {
                is Result.Success -> {
                    result.get()
                }
                is Result.Failure -> {
                    result.getException()
                }
            }

            Assertions.assertThat(model).isInstanceOf(PubgApiWrapper::class.java)
        }
    }
}