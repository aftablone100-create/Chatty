package com.chatty.app.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun authStateFlow(): Flow<String?>
    suspend fun register(email: String, password: String): Result<String>
    suspend fun login(email: String, password: String): Result<String>
    fun logout()
    fun currentUserId(): String?
}
