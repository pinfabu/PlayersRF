package com.pinfabu.playersrf.data

import com.pinfabu.playersrf.data.remote.PlayersApi
import com.pinfabu.playersrf.data.remote.model.PlayersDetailDto
import com.pinfabu.playersrf.data.remote.model.PlayersDto
import retrofit2.Call
import retrofit2.Retrofit

class PlayerRepository(private val retrofit: Retrofit) {

    private val playersApi: PlayersApi = retrofit.create(PlayersApi::class.java)

    fun getPlayers(url: String?): Call<List<PlayersDto>> = playersApi.getPlayers(url)

    fun getPlayerDetail(id: String?): Call<PlayersDetailDto> = playersApi.getPlayerDetail(id)

}