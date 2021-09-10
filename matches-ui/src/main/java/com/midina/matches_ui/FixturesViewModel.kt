package com.midina.matches_ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import com.midina.matches_domain.usecase.GetMatchesSchedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

//TODO parse gif when match is going
//TODO TourHeaders in RecyclerView
//TODO Clean Architecture

private const val DATE_PATTERN = "dd.MM.yyyy HH:mm"
private const val TOUR_SIZE = 30
private const val MATCHES_IN_TOUR = 8
private const val FIRST_MATCH_IN_TOUR  = 0
private const val MATCHES_COUNT = 240

class FixturesViewModel @Inject constructor(private val getMatchesSchedule: GetMatchesSchedule):
    ViewModel() {

    enum class TourFilter{
        SHOW_FIRST,
        SHOW_SECOND,
        SHOW_ALL
    }

    private var matchesMap : Map<Int,List<MatchSchedule>> = mutableMapOf()
    private var dateMap : MutableMap<Int,Pair<LocalDateTime,LocalDateTime>> = mutableMapOf()

    private val sdf by lazy { SimpleDateFormat(DATE_PATTERN) }

    private val _tours = MutableLiveData<Int>()
    val tours : LiveData<Int>
        get() = _tours

    private val _events = SingleLiveEvent<UiEvent>()
    val events: LiveData<UiEvent>
        get() = _events

    init {
        _tours.value = 0
        parse()

    }

    private fun parse(){
        _events.postValue(UiEvent.Loading)

        viewModelScope.launch(Dispatchers.IO){

            val result = getMatchesSchedule.execute()

            when(result){
                is ResultEvent.Success -> {
                    matchesMap = result.value
                    getDateMap()
                    val tourByDate = getTourByDate()
                    _tours.postValue(tourByDate)
                   _events.postValue(matchesMap[tourByDate]?.let { UiEvent.Success(it) })
                }

                is ResultEvent.Error -> _events.postValue(UiEvent.Error)
            }

        }
    }

    private fun showList(filter: TourFilter){
        when(filter){
            TourFilter.SHOW_FIRST -> {
                _tours.value = 1
                _events.value = matchesMap[_tours.value]?.let { UiEvent.Success(it) }
            }
            TourFilter.SHOW_SECOND -> {
                _tours.value = 2
                _events.value = matchesMap[_tours.value]?.let { UiEvent.Success(it) }
            }
            else -> {
                _tours.value = 0
                _events.value = matchesMap[_tours.value]?.let { UiEvent.Success(it) }
            }
        }
    }

    fun updateFilter(filter: TourFilter) {
        showList(filter)
    }

    fun backArrowClicked(){
        _tours.value = _tours.value?.minus(1)
        _tours.value?.let {
            if(it > 0){
                _events.value = matchesMap[it]?.let { mapList -> UiEvent.Success(mapList) }
            }else{
                _events.value = matchesMap[0]?.let { mapList -> UiEvent.Success(mapList) }
            }
        }
    }

    fun nextArrowClicked(){
        _tours.value = _tours.value?.plus(1)
        _events.value = matchesMap[_tours.value]?.let {mapList -> UiEvent.Success(mapList) }
    }


    private fun getTourByDate() : Int{
        val currentDate = sdf.format(Date())
        val date = LocalDateTime.parse(
            currentDate,
            DateTimeFormatter.ofPattern(DATE_PATTERN))

        var tourCount = 1
        for(index in 1 ..TOUR_SIZE){
            if(date.isAfter(dateMap[index]?.second)){
                tourCount = index
            }else{
                if (date.isAfter(dateMap[index]?.first) && date.isBefore(dateMap[index]?.second)){
                    return index
                }
            }
        }

        return if(tourCount == TOUR_SIZE){
            tourCount
        }else{
            tourCount + 1
        }

    }

    //TODO ЭТО НАДО В USECASE?
    private fun getLocaleDateTimePair(first: String, last:String) :
            Pair<LocalDateTime,LocalDateTime>{

        val localFirst = LocalDateTime.parse(
            first,
            DateTimeFormatter.ofPattern(DATE_PATTERN))

        val localLast = LocalDateTime.parse(
            last,
            DateTimeFormatter.ofPattern(DATE_PATTERN))

        return Pair(localFirst,localLast)
    }

    private fun getDateMap(){

        var matchCount = FIRST_MATCH_IN_TOUR
        var tourCount = 1
        for (index in 0 until MATCHES_COUNT){
            matchCount++
            if(matchCount == MATCHES_IN_TOUR){
                //TODO SHOW THIS SHIT
                dateMap[tourCount] = matchesMap[tourCount]?.get(FIRST_MATCH_IN_TOUR)?.let { first ->
                    matchesMap[tourCount]?.get(MATCHES_IN_TOUR-1)?.let { second ->
                        getLocaleDateTimePair(
                            first.date,
                            second.date)
                    }
                }!!
                matchCount = FIRST_MATCH_IN_TOUR
                tourCount++
            }
        }
    }
}


sealed class UiEvent {
    class Success(val matches: List<MatchSchedule>) : UiEvent()
//    class OnTourChanged(val matches: List<MatchSchedule>) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}