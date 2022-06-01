package com.midina.registration_data.usecaseimpl

import com.midina.registration_data.RegisterUserRepository
import com.midina.registration_domain.model.ResultEvent
import com.midina.registration_domain.model.User
import com.midina.registration_domain.usecase.WriteToFirebaseDatastoreUsecase
import javax.inject.Inject

class WriteToFirebaseDatastoreUsecaseImpl @Inject constructor(
    private val registerUserRepository: RegisterUserRepository
) : WriteToFirebaseDatastoreUsecase {
    override suspend fun execute(user: User): ResultEvent<String> {
        return registerUserRepository.writeToFirebaseDataStore(user)
    }
}