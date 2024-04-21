package com.pinfabu.playersrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class PlayersDto(
    @SerializedName("id_jugador")
    val idPlayer: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("url_image")
    val urlImage: String? = null
)
