package com.pinfabu.playersrf.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pinfabu.playersrf.R
import com.pinfabu.playersrf.application.PlayersRFApp
import com.pinfabu.playersrf.data.PlayerRepository
import com.pinfabu.playersrf.data.remote.model.PlayersDetailDto
import com.pinfabu.playersrf.databinding.FragmentPlayerDetailBinding
import com.pinfabu.playersrf.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


private const val PLAYER_ID = "player_id"

class PlayerDetailFragment : Fragment() {
    private var _binding: FragmentPlayerDetailBinding? = null
    private val binding get() = _binding!!

    private var player_id: String? = null

    private lateinit var repository: PlayerRepository

    //Para tener una instancia al mapa de manera global
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            player_id = args.getString(PLAYER_ID)

            Log.d(Constants.LOGTAG, "Id recibido: $player_id")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Reproducir el sonido aquí
        val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.golsound)
        mediaPlayer.start()

        super.onViewCreated(view, savedInstanceState)

        //Programar la conexión
        repository = (requireActivity().application as PlayersRFApp).repository

        lifecycleScope.launch {
            player_id?.let { id ->
                val call: Call<PlayersDetailDto> = repository.getPlayerDetail(id)

                call.enqueue(object: Callback<PlayersDetailDto> {
                    override fun onResponse(p0: Call<PlayersDetailDto>, response: Response<PlayersDetailDto>) {
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE
                            tvTitle.text = response.body()?.name
                            Glide.with(requireActivity())
                                .load(response.body()?.urlImage)
                                .into(ivImage)
                            tvLongDesc.append(response.body()?.team)
                            tvPosition.append(response.body()?.position)
                            tvCountry.append(response.body()?.country)
                            tvAge.append(response.body()?.age.toString())
                            tvFoot.append(response.body()?.foot)
                            tvBaloondor.append(response.body()?.ballondor)
                            // Cargar el video en YouTubePlayerView
                            youtubePlayerView.addYouTubePlayerListener(object :
                                AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    // Cargar el video cuando el reproductor esté listo
                                    response.body()?.ytVideo?.let { videoId ->
                                        youTubePlayer.loadVideo(videoId, 0f)
                                    } ?: run {
                                        // Si no se puede cargar el video, cargar un video por defecto
                                        youTubePlayer.loadVideo("0AwxHCI_BnA", 0f)
                                    }

                                    // Inicializar el mapa después de que el reproductor de YouTube esté listo
                                    response.body()?.let { playerDetail ->
                                        initializeMap(playerDetail.lat ?: 0.0, playerDetail.long ?: 0.0)
                                    }

                                }
                            })


                        }
                    }

                    override fun onFailure(p0: Call<PlayersDetailDto>, p1: Throwable) {
                        //Manejar el error sin conexión
                        binding.apply {
                            pbLoading.visibility = View.INVISIBLE

                            Log.d(Constants.LOGTAG, p1.message.toString())
                        }
                    }

                })
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(gameId: String) =
            PlayerDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PLAYER_ID, gameId)
                }
            }
    }

    private fun initializeMap(lat: Double, long: Double) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Aquí puedes realizar operaciones con el mapa una vez que esté listo
            map = googleMap
            createMarker(lat, long)
            // Por ejemplo, puedes establecer la configuración del mapa aquí
            // map.uiSettings.isZoomControlsEnabled = true
        }
    }


    private fun createMarker(lat: Double, long: Double){
        val coordinates = LatLng(lat, long)

        val marker = MarkerOptions()
            .position(coordinates)
            .title("Estadio jugador")
            .snippet("FIFA")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.school))

        map.addMarker(marker)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 16f),
            4000,
            null
        )
    }
}