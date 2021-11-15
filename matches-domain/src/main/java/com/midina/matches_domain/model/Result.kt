package com.midina.matches_domain.model

sealed class ResultEvent<out T : Any> {
    data class Success<out T : Any>(val value: T) : ResultEvent<T>()
    object Error : ResultEvent<Nothing>()
}