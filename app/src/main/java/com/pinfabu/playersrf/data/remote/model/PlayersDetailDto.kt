package com.pinfabu.playersrf.data.remote.model

import com.google.gson.annotations.SerializedName

data class PlayersDetailDto(
    @SerializedName("id_jugador")
    val idPlayer: Int,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("position")
    val position: String? = null,
    @SerializedName("team")
    val team: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("age")
    val age: Int? = null,
    @SerializedName("foot")
    val foot: String? = null,
    @SerializedName("ballondor")
    val ballondor: String? = null,
    @SerializedName("url_image")
    val urlImage: String? = null,
    @SerializedName("yt_video")
    val ytVideo: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("long")
    val long: Double? = null,
)