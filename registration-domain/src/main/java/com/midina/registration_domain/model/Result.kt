package com.midina.registration_domain.model

sealed class ResultEvent {
    object Success : ResultEvent()
    object InProgress : ResultEvent()
    object Error : ResultEvent()
}