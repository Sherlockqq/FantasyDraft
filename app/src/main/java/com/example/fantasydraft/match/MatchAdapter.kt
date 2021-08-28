package com.example.fantasydraft.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasydraft.R


class MatchAdapter() :
    RecyclerView.Adapter<MatchAdapter.MyViewHolder>() {

    //hashMap key = tour / value = list Matches

    private var list: List<MatchSchedule> = emptyList()
    fun updateMatches(updatedList: List<MatchSchedule>){
        list = updatedList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fixture_item,
            parent,
            false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = list[position]
        holder.tour.text = currentItem.tour
        holder.date.text = currentItem.date
        holder.home.text = currentItem.homeTeam
        holder.guest.text = currentItem.guestTeam
        holder.score.text = currentItem.score

    }

    override fun getItemCount(): Int {
       return list.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val tour : TextView = itemView.findViewById(R.id.tour_number)
        val home : TextView = itemView.findViewById(R.id.home_team)
        val score : TextView = itemView.findViewById(R.id.match_score)
        val guest : TextView = itemView.findViewById(R.id.guest_team)
        val date : TextView = itemView.findViewById(R.id.match_date)
    }
}