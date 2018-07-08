package be.swsb.makeastats.kotlinbackend.pubgacl.pubg

import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model.Player
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model.PubgApiWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import io.reactivex.Single
import org.springframework.stereotype.Service
import java.io.Reader

@Service
class PlayerService(val pubgApiConfig: PubgApiConfig,
                    val objectMapper: ObjectMapper) {

    fun findPlayersByNames(playerNames: List<String>? = null): Single<Result<PubgApiWrapper<List<Player>>, FuelError>> {
        val queryParams:String = playerNameQuery(playerNames)
        return Fuel.get("${pubgApiConfig.baseUrl}/players${queryParams}")
                .header(mapOf(
                "Authorization" to "Bearer: ${pubgApiConfig.apiKey}",
                "Accept" to "application/vnd.api+json"
        )).rx_object(PlayerDeserializer(objectMapper))
    }

    private fun playerNameQuery(playerNames: List<String>? = null): String {
        return playerNames?.joinToString(",","?filter[playerNames]=") ?: ""
    }

    class PlayerDeserializer(private val objectMapper: ObjectMapper): ResponseDeserializable<PubgApiWrapper<List<Player>>> {
        override fun deserialize(reader: Reader): PubgApiWrapper<List<Player>> {
            return objectMapper.readValue(reader)
        }
    }
}