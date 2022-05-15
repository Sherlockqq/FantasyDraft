package com.midina.login_domain.model

sealed class ResultEvent<out T : Any> {
    object Success : ResultEvent<Nothing>()
    data class InvalidateData(val value: String) : ResultEvent<String>()
    object Error : ResultEvent<Nothing>()
}

sealed class EmailResult {
    object Exist: EmailResult()
    object NotExist : EmailResult()
    object Error : EmailResult()
}

sealed class PasswordResetResult {
    object Success : PasswordResetResult()
    object InvalidateData: PasswordResetResult()
    object Error : PasswordResetResult()
}