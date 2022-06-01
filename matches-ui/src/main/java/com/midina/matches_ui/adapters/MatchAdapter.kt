package com.midina.matches_ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_ui.R

class MatchAdapter : RecyclerView.Adapter<MatchAdapter.FixturesHolder>() {

    private var list: List<MatchSchedule> = emptyList()
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, match: MatchSchedule)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun updateMatches(updatedList: List<MatchSchedule>) {
        list = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesHolder {

        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.fixture_item,
            parent,
            false
        )
        return FixturesHolder(
            itemView,
            mListener
        )
    }

    override fun onBindViewHolder(holder: FixturesHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class FixturesHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var match: MatchSchedule? = null

        private val home: TextView = itemView.findViewById(R.id.home_team)
        private val score: TextView = itemView.findViewById(R.id.match_score)
        private val guest: TextView = itemView.findViewById(R.id.guest_team)
        private val date: TextView = itemView.findViewById(R.id.match_date)
        private val homeImage: ImageView = itemView.findViewById(R.id.home_logo)
        private val guestImage: ImageView = itemView.findViewById(R.id.guest_logo)

        init {
            itemView.setOnClickListener {
                this.match?.let { match -> listener.onItemClick(adapterPosition, match) }
            }
        }

        fun bind(item: MatchSchedule) {
            match = item
            date.text = item.date
            home.text = item.homeTeam
            guest.text = item.guestTeam
            score.text = item.score
            Glide.with(itemView).load(item.homeLogo).into(homeImage)
            Glide.with(itemView).load(item.guestLogo).into(guestImage)
        }
    }
}

