package com.pinfabu.playersrf.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.pinfabu.playersrf.data.remote.model.PlayersDto
import com.pinfabu.playersrf.databinding.PlayerElementBinding

class PlayersViewHolder (private var binding: PlayerElementBinding):
    RecyclerView.ViewHolder(binding.root) {
    val ivThumbnail = binding.ivThumbnail

    fun bind(player: PlayersDto){
        binding.tvTitle.text = player.name
    }
}