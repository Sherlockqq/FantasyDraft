package com.midina.matches_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.midina.matches_domain.model.MatchSchedule

const val HEADER_VIEW_TYPE = 111
const val ITEM_VIEW_TYPE = 222

class MatchAdapter : RecyclerView.Adapter<MatchAdapter.MyViewHolder>() {

    private var list: List<MatchSchedule> = emptyList()

    fun updateMatches(updatedList: List<MatchSchedule>){
        list = updatedList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        when(viewType){
            HEADER_VIEW_TYPE -> {
                val itemView: View =LayoutInflater.from(parent.context).inflate(
                    R.layout.fixture_header,
                    parent,
                    false)
                return MyViewHolder.HeaderViewHolder(itemView)
            }
            else -> {
                val itemView: View =LayoutInflater.from(parent.context).inflate(
                    R.layout.fixture_item,
                    parent,
                    false)
                return MyViewHolder.MatchViewHolder(itemView)
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]?.isHeader){
            true -> HEADER_VIEW_TYPE
            else -> ITEM_VIEW_TYPE
        }
    }

    sealed class MyViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: MatchSchedule){}

        class HeaderViewHolder(itemView : View) : MyViewHolder(itemView){
            private val tour : TextView = itemView.findViewById(R.id.tour_header)
            override fun bind(item: MatchSchedule){
                tour.text = "Tour : " + item.tour.toString()
            }
        }

        class MatchViewHolder(itemView: View) : MyViewHolder(itemView) {
            private val home: TextView = itemView.findViewById(R.id.home_team)
            private val score: TextView = itemView.findViewById(R.id.match_score)
            private val guest: TextView = itemView.findViewById(R.id.guest_team)
            private val date: TextView = itemView.findViewById(R.id.match_date)
            private val homeImage: ImageView = itemView.findViewById(R.id.home_logo)
            private val guestImage: ImageView = itemView.findViewById(R.id.guest_logo)

            override fun bind(item: MatchSchedule){
                date.text = item.date
                home.text = item.homeTeam
                guest.text = item.guestTeam
                score.text = item.score

                Glide.with(itemView).load(getImage(item.homeTeam)).into(homeImage)
                Glide.with(itemView).load(getImage(item.guestTeam)).into(guestImage)



                //TODO set config 565
                //Todo уменьшить картинку через фоторедактор а потом tinypng
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
                    "Черноморец" -> return R.drawable.chornomorets_logo
                    "Александрия" -> return R.drawable.oleksandriya_logo
                    "Днепр-1" -> return R.drawable.dnipro1_logo
                    "Минай" -> return R.drawable.minaj_logo
                }
                return R.drawable.connection_error
            }
        }
    }

}

