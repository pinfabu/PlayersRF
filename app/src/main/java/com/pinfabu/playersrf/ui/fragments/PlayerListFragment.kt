package com.pinfabu.playersrf.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pinfabu.playersrf.R
import com.pinfabu.playersrf.application.PlayersRFApp
import com.pinfabu.playersrf.data.PlayerRepository
import com.pinfabu.playersrf.data.remote.model.PlayersDto
import com.pinfabu.playersrf.databinding.FragmentPlayerListBinding
import com.pinfabu.playersrf.ui.Login
import com.pinfabu.playersrf.ui.adapters.PlayerAdapter
import com.pinfabu.playersrf.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerListFragment : Fragment() {

    private var _binding: FragmentPlayerListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: PlayerRepository

    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlayerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //El usuario ya ve el fragment en pantalla
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth?.currentUser
        userId = user?.uid

        binding.tvUsuario.text = user?.email

        //revisamos si el email no está verificado

        if(user?.isEmailVerified != true){
            binding.tvCorreoNoVerificado.visibility = View.VISIBLE
            binding.btnReenviarVerificacion.visibility = View.VISIBLE

            binding.btnReenviarVerificacion.setOnClickListener {
                user?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(requireContext(), "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error: El correo de verificación no se ha podido enviar", Toast.LENGTH_SHORT).show()
                    Log.d("LOGS", "onFailure: ${it.message}")
                }
            }
        }



        //Para cerrar sesión
        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireActivity(), Login::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        repository = (requireActivity().application as PlayersRFApp).repository
        lifecycleScope.launch {
            //val call: Call<List<GameDto>> = repository.getGames("cm/games/games_list.php")
            val call: Call<List<PlayersDto>> = repository.getPlayers("players")

            call.enqueue(object : Callback<List<PlayersDto>> {
                override fun onResponse(p0: Call<List<PlayersDto>>, response: Response<List<PlayersDto>>) {
                    //Respuesta del server

                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, "Respuesta recibida: ${response.body()}")

                    response.body()?.let {players ->
                        binding.rvPlayers.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = PlayerAdapter(players){ player ->
                                //Aquí va la operación para el click de cada elemento
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, PlayerDetailFragment.newInstance(player.idPlayer.toString()))
                                    .addToBackStack(null)
                                    .commit()

                            }

                        }
                    }
                }

                override fun onFailure(p0: Call<List<PlayersDto>>, error: Throwable) {
                    //Manejo del error
                    binding.pbLoading.visibility = View.GONE

                    Toast.makeText(
                        requireContext(),
                        "Error en la conexión: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}