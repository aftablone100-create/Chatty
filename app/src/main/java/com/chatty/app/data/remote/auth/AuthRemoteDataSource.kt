package com.chatty.app.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRemoteDataSource(private val firebaseAuth: FirebaseAuth) {

    fun authStateFlow(): Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.uid)
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    suspend fun register(email: String, password: String): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: error("Registration failed")
    }

    suspend fun login(email: String, password: String): String {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: error("Login failed")
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUserId(): String? = firebaseAuth.currentUser?.uid
}
