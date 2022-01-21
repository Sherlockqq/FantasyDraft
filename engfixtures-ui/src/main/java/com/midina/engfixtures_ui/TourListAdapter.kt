package com.midina.engfixtures_ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midina.engfixtures_domain.model.Match

class TourListAdapter : RecyclerView.Adapter<TourHolder>() {

    private var list: ArrayList<Match> = arrayListOf()


    fun updateMatches(updatedList: ArrayList<Match>) {
        list = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourHolder {

        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.tour_item,
                parent,
                false
            )
        return TourHolder(itemView)
    }

    override fun onBindViewHolder(holder: TourHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class TourHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var match: Match? = null

    private val home: TextView = itemView.findViewById(R.id.tv_homeTeam)
    private val score: TextView = itemView.findViewById(R.id.tv_score)
    private val away: TextView = itemView.findViewById(R.id.tv_awayTeam)
    private val date: TextView = itemView.findViewById(R.id.tv_date)
    private val homeImage: ImageView = itemView.findViewById(R.id.iv_homeTeam)
    private val awayImage: ImageView = itemView.findViewById(R.id.iv_awayTeam)

    @SuppressLint("SetTextI18n")
    fun bind(item: Match) {
        match = item
        date.text = item.date
        home.text = item.homeTeamName
        away.text = item.awayTeamName
        score.text = item.homeTeamGoals.toString() + " : " + item.awayTeamGoals.toString()

        Glide
            .with(itemView)
            .load(item.homeTeamLogo)
            .into(homeImage)

        Glide
            .with(itemView)
            .load(item.awayTeamLogo)
            .into(awayImage)
    }
}

