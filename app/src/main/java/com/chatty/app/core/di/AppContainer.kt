package com.chatty.app.core.di

import com.chatty.app.data.remote.auth.AuthRemoteDataSource
import com.chatty.app.data.repository.AuthRepositoryImpl
import com.chatty.app.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth

// Manual DI (no Hilt) — keeps build simple for a no-PC/CI-only workflow.
object AppContainer {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val authRemoteDataSource by lazy { AuthRemoteDataSource(firebaseAuth) }
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(authRemoteDataSource) }
}
