package com.midina.stat_ui

import com.midina.stat_domain.GetDataUsecase
import com.midina.stat_domain.model.AsyncTopData
import com.midina.stat_domain.model.ResultEvent
import io.reactivex.subjects.PublishSubject

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify


class LeagueStatViewModelTest {

    lateinit var getDataUsecase: GetDataUsecase

    lateinit var getAsyncUsecase: com.midina.stat_domain.GetAsyncUsecase

    lateinit var viewModel: LeagueStatViewModel


    @Before
    fun setUp() {

        //  MockitoAnnotations.initMocks(   this);
        getDataUsecase = Mockito.mock(GetDataUsecase::class.java)
        getAsyncUsecase = Mockito.mock(com.midina.stat_domain.GetAsyncUsecase::class.java)
        viewModel = LeagueStatViewModel(getDataUsecase, getAsyncUsecase)
    }

    @After
    fun tearDown() {
    }

    private fun getFakeResultEvent(): PublishSubject<com.midina.stat_domain.model.ResultEvent<com.midina.stat_domain.model.AsyncTopData>> {
        return PublishSubject.create()
    }

    @Test
    fun usecaseVerifying() {
        `when`(getAsyncUsecase.execute()).thenReturn(getFakeResultEvent())
        verify(getAsyncUsecase).execute().blockingSubscribe()
    }

}