package be.swsb.makeastats.kotlinbackend.pubgacl

import be.swsb.makeastats.kotlinbackend.objectMapper
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.MatchService
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PubgApiConfig
import com.github.kittinunf.result.Result
import org.assertj.core.api.Assertions
import org.junit.Test

class MatchServiceTest {

    val matchService: MatchService = MatchService(PubgApiConfig("http://localhost:3333/pubg-stub", ""), objectMapper())

    @Test
    fun canCallAndMap() {
        matchService.findMatchById("58d1b02c-b331-40d9-bdd8-0cc56f0b030f") { req, res, result ->
            val model: Any = when(result) {
                is Result.Success -> {
                    result.get()
                }
                is Result.Failure -> {
                    result.getException()
                }
            }

            Assertions.assertThat(model).isNotNull()
        }
    }
}