package com.midina.registration_domain.usecase

import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.model.User

interface WriteToRoomDatabaseUsecase {
    suspend fun execute(user: User): ResultEvent<String>
}