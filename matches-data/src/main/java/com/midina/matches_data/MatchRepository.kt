package com.midina.matches_data

import com.midina.matches_domain.model.MatchSchedule
import com.midina.matches_domain.model.ResultEvent
import org.jsoup.Jsoup
import javax.inject.Singleton

private const val BASE_URL = "https://sport.ua/football/results/ukraine/1/calendar"

@Singleton
class MatchRepository {

    fun getMatchMap(): ResultEvent<Map<Int, List<MatchSchedule>>> {
        return try {
            val doc = Jsoup.connect(BASE_URL).get()
            ResultEvent.Success(MatchParse.parse(doc))
        } catch (e: Throwable) {
            ResultEvent.Error
        }
    }
}