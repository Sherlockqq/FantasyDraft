package com.example.fantasydraft.fixtures

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fantasydraft.match.MatchSchedule
import com.example.fantasydraft.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

//CapsLock
private  const val BASE_URL = "https://sport.ua/football/results/ukraine/1/calendar"
private  const val HOME_TEAM_HTML = "div.play__cell.play__team-1"
private  const val guestTeamHtml = "div.play__cell.play__team-2"
private  const val dateHtml = "div.play__cell.play__time"
private  const val scoreHtml = "div.play__cell.play__result"

class FixturesViewModel: ViewModel() {

    private val _events = SingleLiveEvent<UiEvent>()
    val events: LiveData<UiEvent>
        get() = _events

    init {
        parse()
    }

    private fun parse(){
        _events.postValue(UiEvent.Loading)
        viewModelScope.launch(Dispatchers.IO){
            try {
                val doc = Jsoup.connect(BASE_URL).get()

                //Getting Home Teams
                val homeList = getListOfHome(doc)

                //Getting Guest Teams
                val guestList = getListOfGuest(doc)

                //Getting time of match
                val dateList = getListDate(doc)

                //Getting match score
                val scoreList = getListScore(doc)

                if(homeList.isEmpty() || guestList.isEmpty() || dateList.isEmpty() || scoreList.isEmpty()){
                    _events.postValue(UiEvent.Error)
                    //добавить emptyState
                }else{
                    val matchesList = getListMatch(homeList, guestList, dateList, scoreList)
                    _events.postValue(UiEvent.Success(matchesList))
                }

            } catch (e: IOException) {
                e.printStackTrace()
                Log.i("FixturesViewModel","IOException")
                _events.postValue(UiEvent.Error)
            }
        }
    }

    private fun getListOfHome(doc:Document):MutableList<String>{
        val elements = doc.select(HOME_TEAM_HTML)
        val links = elements.select("a")
        val homeTeam : MutableList<String> = mutableListOf()
        for (index in 0 until links.size){
            homeTeam.add(index,links[index].html())
        }
        return homeTeam
    }

    private fun getListOfGuest(doc:Document):MutableList<String>{
        val elements = doc.select(guestTeamHtml)
        val links = elements.select("a")
        val guestTeam : MutableList<String> = mutableListOf()
        for (index in 0 until links.size){
            guestTeam.add(index,links[index].html())
        }
        return guestTeam
    }

    private fun getListDate(doc:Document):MutableList<String>{
        val elements = doc.select(dateHtml)
        val classes = elements.select("div")
        val matchDate : MutableList<String> = mutableListOf()
        for (index in 0 until classes.size){
            matchDate.add(index,classes[index].html())
        }
        return matchDate
    }

    private fun getListScore(doc:Document):MutableList<String>{
        val elements = doc.select(scoreHtml)
        var links = elements.select("a")
        val matchScore : MutableList<String> = mutableListOf()

        for (index in 0 until links.size){
            matchScore.add(index,links[index].html()) // Ended matches
        }
        val size = links.size
        links = elements.select("span")

        for((linkIndex, index) in (size until (links.size + size)).withIndex()){
            matchScore.add(index,links[linkIndex].html())
        }

        return matchScore
    }

    private fun getListMatch(
        home : MutableList<String>,
        guest : MutableList<String>,
        date : MutableList<String>,
        score : MutableList<String>) : MutableList<MatchSchedule>{

        val matchesList : MutableList<MatchSchedule> = mutableListOf()
        var tourCount = 1
        var matchesCount = 1

        for(index in 0 until home.size){
            if(matchesCount == 9){
                tourCount++
                matchesCount = 1
            }

            matchesList.add(index, MatchSchedule("Tour : $tourCount",
                home[index],guest[index],date[index],score[index])
            )
            matchesCount++
        }
        return matchesList
    }
}

sealed class UiEvent {
    class Success(val matches: List<MatchSchedule>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
}