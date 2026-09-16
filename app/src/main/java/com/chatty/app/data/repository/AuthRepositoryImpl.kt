package com.chatty.app.data.repository

import com.chatty.app.data.remote.auth.AuthRemoteDataSource
import com.chatty.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val remote: AuthRemoteDataSource
) : AuthRepository {

    override fun authStateFlow(): Flow<String?> = remote.authStateFlow()

    override suspend fun register(email: String, password: String): Result<String> =
        runCatching { remote.register(email, password) }

    override suspend fun login(email: String, password: String): Result<String> =
        runCatching { remote.login(email, password) }

    override fun logout() = remote.logout()

    override fun currentUserId(): String? = remote.currentUserId()
}
