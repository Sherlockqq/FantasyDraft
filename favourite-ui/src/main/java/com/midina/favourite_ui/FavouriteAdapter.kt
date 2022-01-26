package com.midina.favourite_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midina.favourite_domain.Team

const val ODD_VIEW_TYPE = 111
const val EVEN_VIEW_TYPE = 222

//TODO HEADER and BOTTOM space

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.TeamHolder>() {

    private var teamList: List<Team> = emptyList()


    private lateinit var itemListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, team: Team)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemListener = listener
    }

    fun updateTeams(updatedList: List<Team>) {
        teamList = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamHolder {
        when (viewType) {
            EVEN_VIEW_TYPE -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.even_item,
                    parent,
                    false
                )
                return TeamHolder.EvenViewHolder(itemView, itemListener)
            }
            else -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(
                    R.layout.odd_item,
                    parent,
                    false
                )
                return TeamHolder.OddViewHolder(itemView, itemListener)
            }
        }
    }

    override fun onBindViewHolder(holder: TeamHolder, position: Int) {
        holder.bind(teamList[position])
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) {
            EVEN_VIEW_TYPE
        } else {
            ODD_VIEW_TYPE
        }
    }

    sealed class TeamHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var team: Team? = null

        open fun bind(item: Team) {}

        class EvenViewHolder(
            itemView: View,
            listener: OnItemClickListener
        ) : TeamHolder(itemView) {

            private val teamName: TextView = itemView.findViewById(R.id.tv_team_name)
            private val teamImage: ImageView = itemView.findViewById(R.id.iv_team_logo)

            override fun bind(item: Team) {
                team = item
                teamName.text = item.name
                Glide.with(itemView).load(getImage(item.name)).into(teamImage)
            }

            init {
                itemView.setOnClickListener {
                    this.team?.let { team -> listener.onItemClick(adapterPosition, team) }
                }
            }
        }

        class OddViewHolder(
            itemView: View,
            listener: OnItemClickListener
        ) : TeamHolder(itemView) {

            private val teamName: TextView = itemView.findViewById(R.id.tv_team_name)
            private val teamImage: ImageView = itemView.findViewById(R.id.iv_team_logo)

            init {
                itemView.setOnClickListener {
                    this.team?.let { team -> listener.onItemClick(adapterPosition, team) }
                }
            }

            override fun bind(item: Team) {
                team = item
                teamName.text = item.name
                Glide.with(itemView).load(getImage(item.name)).into(teamImage)
            }
        }

        fun getImage(team: String): Int {
            when (team) {
                "Львов" -> return R.drawable.lviv_logo
                "Верес" -> return R.drawable.veres_logo
                "Шахтер Донецк" -> return R.drawable.shakhtar_logo
                "Металлист 1925" -> return R.drawable.metallist25_logo
                "Десна" -> return R.drawable.desna_logo
                "Заря" -> return R.drawable.zarya_logo
                "Ворскла" -> return R.drawable.vorskla_logo
                "Динамо Киев" -> return R.drawable.dynamo_logo
                "Мариуполь" -> return R.drawable.mariupol_logo
                "Колос К" -> return R.drawable.kolos_logo
                "Ингулец" -> return R.drawable.ingulets_logo
                "Рух Львов" -> return R.drawable.rukh_logo
                "Черноморец" -> return R.drawable.chornomorets_logo
                "Александрия" -> return R.drawable.oleksandriya_logo
                "Днепр-1" -> return R.drawable.dnipro1_logo
                "Минай" -> return R.drawable.minaj_logo
            }
            return R.drawable.connection_error
        }
    }
}