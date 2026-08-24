package com.tayler.playvalu.repository.network

import com.tayler.playvalu.model.JamendoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApi {
    @GET("v3.0/tracks/")
    suspend fun searchTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("namesearch") query: String,
        @Query("limit") limit: Int = 20,
        @Query("audioformat") audioFormat: String = "mp32"
    ): JamendoResponse
}
