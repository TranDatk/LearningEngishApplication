package com.tnmd.learningenglishapp.screens.login

import com.tnmd.learningenglishapp.data.StreamTokenApi
import io.getstream.chat.android.client.token.TokenProvider
import kotlinx.coroutines.runBlocking

class StreamTokenProvider(private val api: StreamTokenApi) {
    suspend fun getTokenProvider(userId: String): com.tnmd.learningenglishapp.screens.login.TokenProvider {
        val tokenResponse = api.getToken(userId)
        val token = tokenResponse.token

        return TokenProvider(token)
    }
}
