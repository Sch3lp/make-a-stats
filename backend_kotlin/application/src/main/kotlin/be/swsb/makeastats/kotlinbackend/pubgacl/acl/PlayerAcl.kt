package be.swsb.makeastats.kotlinbackend.pubgacl.acl

import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.MatchService
import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PlayerService
import com.github.kittinunf.result.Result
import org.springframework.stereotype.Service

@Service
class PlayerAcl(val playerService: PlayerService,
                val matchService: MatchService) {

    fun triggerPlayerUpdate(names: List<String>) {
        playerService.findPlayersByNames(names) { req, res, result ->
            when(result) {
                is Result.Success -> {
                    val playerMatchIds = result.get().data?.map { it.to(it.matchIds) }
                    playerMatchIds?.map {
                        it.second.map {
                            matchService.findMatchById(it) { req, res, result ->
                                when(result) {
                                    is Result.Success -> {
                                        result.get().data?.attributes //oh great, the model I imported does not have Match properly implemented, it leaves out the Roster.kt type...
                                    }

                                    is Result.Failure -> {
                                        // noop ? I dunno
                                    }
                                }
                            }
                        }
                    }
                }

                is Result.Failure -> {
                    // retry or some shit? I dunno
                }

            }
        }
//                .subscribe({ result: Result<PubgApiWrapper<List<Player>>, FuelError>? ->
//                    result?.fold({ wrapper ->
//                        wrapper.data?.map { player ->
//                            player.matchIds.map({ matchService.findMatchById(it.id)})
//                        }
//                    }, { err ->
//                        emptyList()
//                    })
//                })

    }
}