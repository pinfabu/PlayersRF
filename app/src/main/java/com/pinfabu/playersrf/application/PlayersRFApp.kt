package com.pinfabu.playersrf.application

import android.app.Application
import com.pinfabu.playersrf.data.PlayerRepository
import com.pinfabu.playersrf.data.remote.RetrofitHelper

class PlayersRFApp: Application() {
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy { PlayerRepository(retrofit) }
}