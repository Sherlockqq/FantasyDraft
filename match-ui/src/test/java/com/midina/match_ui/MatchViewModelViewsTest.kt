package com.midina.match_ui

import android.os.Bundle
import com.midina.android.match_domain.model.MatchWeather
import com.midina.android.match_domain.model.ResultEvent
import com.midina.android.match_domain.usecase.GetWeatherUsecase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MatchViewModelTest {

    private lateinit var viewModel: MatchViewModel

    private lateinit var usecase: GetWeatherUsecase

    private lateinit var bundle: Bundle

    private val matchWeather = MatchWeather(
        lat = 49.84F,
        lon = 24.02F,
        weather = "weather",
        temperature = 36.6,
        date = "12.01.2022 19:30"
    )

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        usecase = mockk(relaxed = true)
        bundle = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
    }

    private fun validBundle() {
        every { bundle.getString("HomeTeam") } returns "Львов"
        every { bundle.getString("GuestTeam") } returns "Верес"
        every { bundle.getString("Score") } returns "1 : 0"
        every { bundle.getString("Date") } returns "12.01.2022 19:30"
    }

    private fun invalidBundle() {
        every { bundle.getString("HomeTeam") } returns ""
        every { bundle.getString("GuestTeam") } returns ""
        every { bundle.getString("Score") } returns ""
        every { bundle.getString("Date") } returns ""
    }

    private fun getFullTeams(): Array<String> {
        return arrayOf(
            "Львов",
            "Верес",
            "Шахтер Донецк",
            "Металлист 1925",
            "Десна",
            "Заря",
            "Ворскла",
            "Динамо Киев",
            "Мариуполь",
            "Колос К",
            "Ингулец",
            "Рух Львов",
            "Черноморец",
            "Александрия",
            "Днепр-1",
            "Минай",
            ""
        )
    }

    private fun getTeamName() : Array<String> {
        return arrayOf(
            "ЛЬВ",
            "ВЕР",
            "ШАХ",
            "МЕТ",
            "ДЕС",
            "ЗАР",
            "ВОР",
            "ДИН",
            "МАР",
            "КОЛ",
            "ИНГ",
            "РУХ",
            "ЧЕР",
            "АЛЕ",
            "ДНЕ",
            "МИН",
            "КОМ"
        )
    }

    private fun getCoordinates(): Array<Pair<Float, Float>> {
        return arrayOf(
            Pair(49.84F, 24.02F),
            Pair(50.61F, 26.25F),
            Pair(50.44F, 30.52F),
            Pair(49.99F, 36.23F),
            Pair(51.49F, 31.28F),
            Pair(47.83F, 35.14F),
            Pair(49.58F, 34.55F),
            Pair(50.44F, 30.52F),
            Pair(47.09F, 37.54F),
            Pair(49.98F, 30.01F),
            Pair(48.34F, 33.26F),
            Pair(49.84F, 24.02F),
            Pair(46.47F, 30.73F),
            Pair(48.67F, 33.10F),
            Pair(48.46F, 35.04F),
            Pair(48.59F, 22.28F),
            Pair(0.0F, 0.0F)

        )
    }

    @Test
    fun checkGetTeamsName() {
        val fullTeamArray = getFullTeams()
        val teamArray = getTeamName()

        for(i in teamArray.indices){
            viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

            assertEquals(teamArray[i], viewModel.getTeamName(fullTeamArray[i]))
        }
    }

    @Test
    fun checkCoordinates() {
        val teamArray = getFullTeams()
        val coordinatesArray = getCoordinates()

        for (i in teamArray.indices) {
            every { bundle.getString("HomeTeam") } returns teamArray[i]

            viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

            assertEquals(coordinatesArray[i].first, viewModel.lat.value)
            assertEquals(coordinatesArray[i].second, viewModel.lon.value)
        }
    }

    @Test
    fun getDataSuccess() = rule.runBlockingTest {
        coEvery {
            usecase.execute(any(), any(), any())
        } returns ResultEvent.Success(matchWeather)

        viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

        assertTrue(viewModel.matchEvents.value is UiEvent.Success)
        assertEquals(matchWeather, (viewModel.matchEvents.value as UiEvent.Success).weather)
    }

    @Test
    fun getDataEmpty() = rule.runBlockingTest {
        coEvery {
            usecase.execute(any(), any(), any())
        } returns ResultEvent.EmptyState

        viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

        assertTrue(viewModel.matchEvents.value is UiEvent.EmptyState)
    }

    @Test
    fun getDataError() = rule.runBlockingTest {
        coEvery {
            usecase.execute(any(), any(), any())
        } returns ResultEvent.Error

        viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

        assertTrue(viewModel.matchEvents.value is UiEvent.Error)
    }

    @Test
    fun getHasScoreEvent() {
        every { bundle.getString("Score") } returns "1 : 0"

        viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

        assertEquals(UiScoreOrDateEvent.HasScore, viewModel.scoreOrDateEvents.value)
    }

    @Test
    fun getHasDateEvent() = rule.runBlockingTest {
        every { bundle.getString("Score") } returns "? : ?"
        every { bundle.getString("Date") } returns "12.02.2022 19:30"

        viewModel = MatchViewModel(usecase, rule.dispatcher, bundle)

        assertEquals(UiScoreOrDateEvent.HasDate, viewModel.scoreOrDateEvents.value)
    }
}