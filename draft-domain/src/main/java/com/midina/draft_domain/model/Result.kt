package com.midina.draft_domain.model

sealed class ResultEvent<out T : Any> {
    data class Verified(val email: String) : ResultEvent<String>()
    data class NotVerified(val email: String) : ResultEvent<String>()
    object NotSigned : ResultEvent<Nothing>()

}

sealed class ResultSending {
    object Success: ResultSending()
    object Error: ResultSending()
}