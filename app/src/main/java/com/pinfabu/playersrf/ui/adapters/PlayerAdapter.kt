package com.pinfabu.playersrf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pinfabu.playersrf.data.remote.model.PlayersDto
import com.pinfabu.playersrf.databinding.PlayerElementBinding

class PlayerAdapter (
    private val players: List<PlayersDto>,
    private val onPlayerClicked: (PlayersDto) -> Unit
): RecyclerView.Adapter<PlayersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {
        val binding = PlayerElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayersViewHolder(binding)
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        val player = players[position]
        holder.bind(player)

        //Cargamos con glide la imagen al imageView
        Glide.with(holder.itemView.context)
        Glide.with(holder.itemView.context)
            .load(player.urlImage)
            .into(holder.ivThumbnail)

        //Con Picasso


        holder.itemView.setOnClickListener {
            onPlayerClicked(player)
        }
    }

}