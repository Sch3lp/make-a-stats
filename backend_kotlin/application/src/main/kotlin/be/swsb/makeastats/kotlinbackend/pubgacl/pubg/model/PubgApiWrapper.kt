package be.swsb.makeastats.kotlinbackend.pubgacl.pubg.model

data class PubgApiWrapper<out T>(
        val data: T?,
        val errors: Errors?,
        val links: Links?,
        val included: Any?,
        val meta: Any?
)