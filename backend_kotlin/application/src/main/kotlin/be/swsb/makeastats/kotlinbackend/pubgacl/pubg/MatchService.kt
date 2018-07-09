package be.swsb.makeastats.kotlinbackend.pubgacl.pubg

import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model.Match
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model.PubgApiWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.result.Result
import org.springframework.stereotype.Service
import java.io.Reader

@Service
class MatchService(val pubgApiConfig: PubgApiConfig,
                   val objectMapper: ObjectMapper) {

    fun findMatchById(matchId: String,
                      handler: (Request, Response, Result<PubgApiWrapper<Match>, FuelError>) -> Unit)
        : Request {
        return Fuel.get("${pubgApiConfig.baseUrl}/matches/${matchId}")
                .header(mapOf(
                "Authorization" to "Bearer: ${pubgApiConfig.apiKey}",
                "Accept" to "application/vnd.api+json"
        )).responseObject(MatchDeserializer(objectMapper), handler)
    }

    class MatchDeserializer(private val objectMapper: ObjectMapper): ResponseDeserializable<PubgApiWrapper<Match>> {
        override fun deserialize(reader: Reader): PubgApiWrapper<Match> {
            return objectMapper.readValue(reader)
        }
    }
}