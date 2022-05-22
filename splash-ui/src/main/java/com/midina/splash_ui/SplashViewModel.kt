package com.midina.splash_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.midina.splash_domain.model.ResultEvent
import com.midina.splash_domain.usecase.GetSeasonUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getSeasonUsecase: GetSeasonUsecase
) : ViewModel() {

    private val _events = MutableStateFlow<UiEvent>(UiEvent.Loading)
    val events: StateFlow<UiEvent>
        get() = _events.asStateFlow()

    init {
        seasonRequest()
    }

    private fun seasonRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSeasonUsecase.execute()
            when (result) {
                ResultEvent.EmptyState -> _events.value = UiEvent.EmptyState
                ResultEvent.Error -> _events.value = UiEvent.Error
                is ResultEvent.Success -> {
                    _events.value = UiEvent.Success(result.value)
                }
            }
        }
    }
}

sealed class UiEvent {
    class Success(val season: Int) : UiEvent()
    object Error : UiEvent()
    object Loading : UiEvent()
    object EmptyState : UiEvent()
}