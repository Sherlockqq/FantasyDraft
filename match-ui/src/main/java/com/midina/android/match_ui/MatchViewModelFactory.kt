package com.midina.android.match_ui

import androidx.lifecycle.ViewModel
import com.midina.android.match_domain.usecase.GetWeather
import com.midina.core_ui.di.ViewModelFactory
import javax.inject.Inject
import javax.inject.Provider

class MatchViewModelFactory @Inject constructor(
    creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
    val getWeather: GetWeather,
    val homeTeam: String) : ViewModelFactory(creators) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  MatchViewModel(getWeather,homeTeam) as T
    }
}