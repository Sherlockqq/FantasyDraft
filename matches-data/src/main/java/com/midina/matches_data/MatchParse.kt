package com.midina.matches_data

import android.util.Log
import com.midina.matches_domain.model.MatchSchedule
import org.jsoup.nodes.Document
import java.time.LocalDateTime

private const val HOME_TEAM_HTML = "div.play__cell.play__team-1"
private const val GUEST_TEAM_HTML = "div.play__cell.play__team-2"
private const val DATE_HTML = "div.play__cell.play__time"
private const val SCORE_HTML = "div.play__cell.play__result"
private const val MATCHES_IN_TOUR = 8
private const val TOUR_SIZE = 30


object MatchParse {

    private var dateMap: MutableMap<Int, Pair<LocalDateTime, LocalDateTime>> = mutableMapOf()

    fun parse(doc: Document): Map<Int, ArrayList<MatchSchedule>> {

        //Getting Home Teams
        val homeList = getListOfHome(doc)

        //Getting Guest Teams
        val guestList = getListOfGuest(doc)

        //Getting time of match
        val dateList = getListDate(doc)

        //Getting match score
        val scoreList = getListScore(doc)

        val matchesList = getListMatch(homeList, guestList, dateList, scoreList)

        return getMapMatch(matchesList)

    }

    private fun getMapMatch(matchesList: ArrayList<MatchSchedule>): Map<Int, ArrayList<MatchSchedule>> {
        val matchesMap: MutableMap<Int, ArrayList<MatchSchedule>> = mutableMapOf()
        for (index in 0..TOUR_SIZE) {
            if (index == 0) {
                matchesMap[index] = getFullMapMatch(matchesList)
            } else {
                matchesMap[index] = getOneTourList(index, matchesList)
            }
        }
        return matchesMap
    }

    private fun getOneTourList(
        tour: Int?,
        matchesList: ArrayList<MatchSchedule>
    ): ArrayList<MatchSchedule> {
        var index = 0
        val tourList: ArrayList<MatchSchedule> = arrayListOf()
        for (i in matchesList.indices) {
            if (matchesList[i].tour == tour) {
                tourList.add(
                    index, MatchSchedule(
                        i, matchesList[i].tour,
                        matchesList[i].homeTeam,
                        matchesList[i].guestTeam,
                        matchesList[i].date,
                        matchesList[i].score
                    )
                )
                index++
            }
        }
        return tourList
    }

    private fun getListMatch(
        home: ArrayList<String>,
        guest: ArrayList<String>,
        date: ArrayList<String>,
        score: ArrayList<String>
    ): ArrayList<MatchSchedule> {

        val matchesList: ArrayList<MatchSchedule> = arrayListOf()
        var tourCount = 1
        var matchesCount = 0

        for (index in 0 until home.size) {
            if (matchesCount == MATCHES_IN_TOUR) {
                tourCount++
                matchesCount = 0
            }

            matchesList.add(
                index, MatchSchedule(
                    index, tourCount,
                    home[index], guest[index], date[index], score[index]
                )
            )
            matchesCount++
        }
        return matchesList
    }

    private fun getListOfHome(doc: Document): ArrayList<String> {
        val elements = doc.select(HOME_TEAM_HTML)
        val links = elements.select("a")
        val homeTeam: ArrayList<String> = arrayListOf()
        for (index in 0 until links.size) {
            homeTeam.add(index, links[index].html())
        }
        return homeTeam
    }

    private fun getListOfGuest(doc: Document): ArrayList<String> {
        val elements = doc.select(GUEST_TEAM_HTML)
        val links = elements.select("a")
        val guestTeam: ArrayList<String> = arrayListOf()
        for (index in 0 until links.size) {
            guestTeam.add(index, links[index].html())
        }
        return guestTeam
    }

    private fun getListDate(doc: Document): ArrayList<String> {
        val elements = doc.select(DATE_HTML)
        val classes = elements.select("div")
        val img = classes.select("img")
        val src = img.attr("src")
        Log.i("DFDs", "FSDFDS")
        val matchDate: ArrayList<String> = arrayListOf()
//        var matchCount = FIRST_MATCH_IN_TOUR
//        var tourCount = 1
        for (index in 0 until classes.size) {
            matchDate.add(index, classes[index].attr("title"))
//            matchCount++
//            if(matchCount == MATCHES_IN_TOUR){
//                dateMap[tourCount] = getLocaleDateTimePair(classes[index].attr("title"),
//                    classes[index-7].attr("title"))
//                matchCount = FIRST_MATCH_IN_TOUR
//                tourCount++
//            }
        }
        return matchDate
    }

    private fun getListScore(doc: Document): ArrayList<String> {
        val elements = doc.select(SCORE_HTML)
        var links = elements.select("a")
        val matchScore: ArrayList<String> = arrayListOf()

        for (index in 0 until links.size) {
            matchScore.add(index, links[index].html()) // Ended matches
        }
        val size = links.size
        links = elements.select("span")

        for ((linkIndex, index) in (size until (links.size + size)).withIndex()) {
            matchScore.add(index, links[linkIndex].html())
        }

        return matchScore
    }

    private fun getFullMapMatch(matches: ArrayList<MatchSchedule>): ArrayList<MatchSchedule> {
        val matchesList: ArrayList<MatchSchedule> = arrayListOf()
        var tourCount = 0
        var matchesIndex = 0
        for (index in 0..268) {
            if (tourCount != matches[matchesIndex].tour) {
                tourCount++
                matchesList.add(
                    index, MatchSchedule(
                        index, tourCount, "",
                        "", "", "", true
                    )
                )
            } else {
                matchesList.add(
                    index, MatchSchedule(
                        index,
                        tourCount,
                        matches[matchesIndex].homeTeam,
                        matches[matchesIndex].guestTeam,
                        matches[matchesIndex].date,
                        matches[matchesIndex].score,
                        matches[matchesIndex].isHeader
                    )
                )
                matchesIndex++
            }
        }
        return matchesList
    }

}