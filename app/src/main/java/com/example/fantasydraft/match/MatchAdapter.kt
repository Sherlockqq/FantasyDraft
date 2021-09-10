package com.example.fantasydraft.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasydraft.R
import com.midina.core_match_domain.MatchSchedule


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
        holder.tour.text = "Tour : " + currentItem.tour.toString()
        holder.date.text = currentItem.date
        holder.home.text = currentItem.homeTeam
        holder.guest.text = currentItem.guestTeam
        holder.score.text = currentItem.score

        holder.homeImage.setImageResource(getImage(currentItem.homeTeam))
        holder.guestImage.setImageResource(getImage(currentItem.guestTeam))

    }

    private fun getImage(team : String): Int{
        when(team){
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
            "Черноморец" -> return R.drawable.chornomoets_logo
            "Александрия" -> return R.drawable.oleksandriya_logo
            "Днепр-1" -> return R.drawable.dnipro1_logo
            "Минай" -> return R.drawable.minaj_logo
        }
        return R.drawable.connection_error
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
        val homeImage : ImageView = itemView.findViewById(R.id.home_logo)
        val guestImage : ImageView = itemView.findViewById(R.id.guest_logo)
    }

}

