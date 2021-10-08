package com.midina.android.match_domain.model

sealed class ResultEvent<out T: Any> {
    data class Success<out T: Any>(val value: T): ResultEvent<T>()
    object EmptyState: ResultEvent<Nothing>()
    object Error: ResultEvent<Nothing>()
}