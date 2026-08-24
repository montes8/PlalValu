package com.tayler.playvalu.repository.search

import com.tayler.playvalu.model.JamendoResponse
import com.tayler.playvalu.repository.network.JamendoApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val jamendoApi: JamendoApi
) {
    suspend fun searchMusic(query: String): JamendoResponse {
        return jamendoApi.searchTracks(clientId = "f6df8b88", query = query)
    }
}
