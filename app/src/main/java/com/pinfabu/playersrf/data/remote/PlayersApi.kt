package com.pinfabu.playersrf.data.remote

import com.pinfabu.playersrf.data.remote.model.PlayersDetailDto
import com.pinfabu.playersrf.data.remote.model.PlayersDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PlayersApi {

    @GET
    fun getPlayers(
        @Url url: String?
    ): Call<List<PlayersDto>> //Así se llamaría: getPlayers("players")

    @GET("players/{id}")
    fun getPlayerDetail(
        @Path("id") id: String?,
    ): Call<PlayersDetailDto> //Se llamaría a la función: getPlayerDetail("1")
}