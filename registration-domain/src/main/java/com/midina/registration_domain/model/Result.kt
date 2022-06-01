package com.midina.registration_domain.model

sealed class ResultEvent<out T : Any> {
    object Success : ResultEvent<Nothing>()
    object InProgress : ResultEvent<Nothing>()
    data class Error(val value: String?) : ResultEvent<String>()
}