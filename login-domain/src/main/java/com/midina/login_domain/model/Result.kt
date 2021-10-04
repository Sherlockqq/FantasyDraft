package com.midina.login_domain.model

sealed class ResultEvent {
    object Success: ResultEvent()
    object InvalidateData: ResultEvent()
    object Error: ResultEvent()
}