package com.midina.draft_domain.model

sealed class ResultEvent {
    object Signed : ResultEvent()
    object NotSigned : ResultEvent()

}