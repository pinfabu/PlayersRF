package com.pinfabu.playersrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.pinfabu.playersrf.R
import com.pinfabu.playersrf.databinding.SplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var binding: SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aquí puedes agregar cualquier lógica necesaria antes de pasar a la actividad principal

        // Ejemplo: espera 2 segundos y luego abre la actividad principal
        Handler().postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 milisegundos = 2 segundos
    }
}