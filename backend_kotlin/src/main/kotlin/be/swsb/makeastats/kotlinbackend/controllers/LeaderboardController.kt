package be.swsb.makeastats.kotlinbackend.controllers

import be.swsb.makeastats.kotlinbackend.model.CreateLeaderBoardCmd
import be.swsb.makeastats.kotlinbackend.model.Leaderboard
import be.swsb.makeastats.kotlinbackend.services.LeaderboardService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("leaderboard")
class LeaderboardController(val leaderboardService: LeaderboardService) {

    @GetMapping("{id}")
    fun getLeaderboard(@PathVariable(value = "id", required = true) id: String): ResponseEntity<Leaderboard> {
        return leaderboardService.getById(id)
                ?.let { ResponseEntity.ok().body(it) }
                ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createLeaderboard(@RequestBody cmd: CreateLeaderBoardCmd, uriBuilder: UriComponentsBuilder): ResponseEntity<Any> {
        val leaderboard = leaderboardService.handle(cmd)

        val location = uriBuilder.path("leaderboard/{id}")
                .buildAndExpand(leaderboard.lid)
                .toUri()

        return ResponseEntity.created(location)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Location")
                .build();
    }
}