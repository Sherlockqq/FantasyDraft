package com.midina.registration_domain.model

sealed class ResultEvent {
    object Success: ResultEvent()
    object InvalidData: ResultEvent()
    object Error: ResultEvent()
}