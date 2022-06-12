package com.midina.team_ui.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midina.club_domain.model.player.Player
import com.midina.team_ui.R

private const val HEADER_VIEW_TYPE = 111
private const val PLAYER_VIEW_TYPE = 222

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayerHolder>() {

    private var playerList: List<Player> = emptyList()

    fun updatePlayers(updatedList: List<Player>) {
        playerList = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        when (viewType) {
            HEADER_VIEW_TYPE -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.player_position,
                    parent,
                    false
                )
                return PlayerHolder.HeaderViewHolder(itemView)
            }
            else -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.player_information,
                    parent,
                    false
                )
                return PlayerHolder.PlayerViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        holder.bind(playerList[position])
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (playerList[position].isHeader) {
            HEADER_VIEW_TYPE
        } else {
            PLAYER_VIEW_TYPE
        }
    }

    sealed class PlayerHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        open fun bind(item: Player) {}

        class HeaderViewHolder(
            itemView: View,
        ) : PlayerHolder(itemView) {

            private val pos: TextView = itemView.findViewById(R.id.tv_position)

            override fun bind(item: Player) {
                pos.text = item.position
            }
        }

        class PlayerViewHolder(
            itemView: View
        ) : PlayerHolder(itemView) {

            private val playerPhoto: ImageView = itemView.findViewById(R.id.iv_photo)
            private val playerName: TextView = itemView.findViewById(R.id.tv_name)
            private val playerCountry: TextView = itemView.findViewById(R.id.tv_country)

            override fun bind(item: Player) {
                playerName.text = item.name
                ("From: " + item.nationality).also { playerCountry.text = it }
                Glide.with(itemView).load(item.photo).into(playerPhoto)
            }
        }
    }
}