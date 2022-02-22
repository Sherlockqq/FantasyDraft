package com.midina.match_data

import com.midina.android.match_domain.model.ResultEvent
import com.midina.match_data.api.WeatherApiInterface
import com.midina.match_data.data.Main
import com.midina.match_data.data.RetroWeather
import com.midina.match_data.data.Weather
import com.midina.match_data.data.WeatherDescription
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherRepositoryTest {

    private lateinit var apiInterface: WeatherApiInterface
    private lateinit var repository: WeatherRepository
    private lateinit var retroWeather: RetroWeather
    private lateinit var list: List<WeatherDescription>
    private val testingDate = "10.12.2021 17:00"
    private val testingLat = 47.09f
    private val testingLon = 37.54F

    @get:Rule
    val rule = MainCoroutineRule()

    @Before
    fun setUp() {
        apiInterface = mockk(relaxed = true)
        retroWeather = mockk(relaxed = true)
        list = mockk(relaxed = true)
        repository = WeatherRepository(apiInterface)
    }

    @After
    fun tearDown() {
    }

    private fun getWeatherDescription(date: String): WeatherDescription {
        return WeatherDescription(
            clouds = mockk(relaxed = true),
            dt = mockk(relaxed = true),
            dtTxt = date,
            main = Main(
                feelsLike = mockk(relaxed = true),
                grndLevel = mockk(relaxed = true),
                humidity = mockk(relaxed = true),
                pressure = mockk(relaxed = true),
                seaLevel = mockk(relaxed = true),
                temp = 273.0,
                tempKf = mockk(relaxed = true),
                tempMax = mockk(relaxed = true),
                tempMin = mockk(relaxed = true)
            ),
            pop = mockk(relaxed = true),
            sys = mockk(relaxed = true),
            visibility = mockk(relaxed = true),
            weather = listOf(
                Weather(
                    description ="",
                    icon = "",
                    id = mockk(relaxed = true),
                    main = "Rain"
                )
            ),
            wind = mockk(relaxed = true)
        )
    }

    private fun getListOfWeatherDescription(): List<WeatherDescription> {
        return listOf(
            getWeatherDescription("2021-12-10 15:00:00"),
            getWeatherDescription( "2021-12-10 18:00:00"),
            getWeatherDescription("2021-12-10 21:00:00"),
            getWeatherDescription("2021-12-11 00:00:00"),
            getWeatherDescription("2021-12-11 03:00:00"),
            getWeatherDescription("2021-12-11 06:00:00"),
            getWeatherDescription("2021-12-11 09:00:00"),
            getWeatherDescription("2021-12-11 12:00:00"),
            getWeatherDescription("2021-12-11 15:00:00"),
            getWeatherDescription("2021-12-11 18:00:00"),
            getWeatherDescription("2021-12-11 21:00:00"),
            getWeatherDescription("2021-12-12 00:00:00")
        )
    }

    private fun getFakeListOfWeatherDescription(): List<WeatherDescription> {
        return listOf(
            getWeatherDescription("2021-12-08 15:00:00"),
            getWeatherDescription( "2021-12-08 18:00:00"),
            getWeatherDescription("2021-12-08 21:00:00"),
            getWeatherDescription("2021-12-09 00:00:00"),
            getWeatherDescription("2021-12-09 03:00:00"),
            getWeatherDescription("2021-12-09 06:00:00"),
            getWeatherDescription("2021-12-09 09:00:00"),
            getWeatherDescription("2021-12-09 12:00:00"),
            getWeatherDescription("2021-12-09 15:00:00"),
            getWeatherDescription("2021-12-09 18:00:00"),
            getWeatherDescription("2021-12-09 21:00:00"),
            getWeatherDescription("2021-12-10 00:00:00")
        )
    }

    @Test
    fun getDataEmpty() = rule.runBlockingTest {
        val listOfWeatherDescription = getFakeListOfWeatherDescription()

        coEvery { apiInterface.getCityWeather(any(), any()) } returns retroWeather
        coEvery { retroWeather.list } returns listOfWeatherDescription

        val result = repository.getWeather(testingLat, testingLon, testingDate)
        assertTrue(result is ResultEvent.EmptyState)
    }

    @Test
    fun getDataSuccess() = rule.runBlockingTest {
        val listOfWeatherDescription = getListOfWeatherDescription()

        coEvery { apiInterface.getCityWeather(any(), any()) } returns retroWeather
        coEvery { retroWeather.list } returns listOfWeatherDescription

        val result = repository.getWeather(testingLat, testingLon, testingDate)
        assertTrue(result is ResultEvent.Success)
        (result as ResultEvent.Success).value.apply {
            assertEquals(testingLat, this.lat)
            assertEquals(testingLon, this.lon)
            assertEquals(testingDate, this.date)
            assertEquals(0.0, this.temperature, 0.0)
            assertEquals("Rain", this.weather)
        }
    }

    @Test
    fun getDataError() = rule.runBlockingTest {

        coEvery { apiInterface.getCityWeather(any(), any()) } throws Exception()

        val result = repository.getWeather(47.09F, 37.54F, testingDate)

        assertTrue(result is ResultEvent.Error)
    }
}