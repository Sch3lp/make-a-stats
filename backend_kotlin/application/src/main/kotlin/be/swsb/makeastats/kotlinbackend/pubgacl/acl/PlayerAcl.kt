package be.swsb.makeastats.kotlinbackend.pubgacl.acl

import be.swsb.makeastats.kotlinbackend.pubgacl.pubg.PlayerService
import org.springframework.stereotype.Service

@Service
class PlayerAcl(val playerService: PlayerService) {

    fun triggerPlayerUpdate(names: List<String>) {

    }
}