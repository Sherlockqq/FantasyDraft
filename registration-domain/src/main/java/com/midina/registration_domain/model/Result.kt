package com.midina.registration_domain.model

sealed class ResultEvent {
    object Success : ResultEvent()
    object UserExist : ResultEvent()
    object Error : ResultEvent()
}