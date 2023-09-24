package com.tnmd.learningenglishapp.screens.chatgpt

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.lembergsolutions.retrofitretry.api.RetryOnError
import com.tnmd.learningenglishapp.screens.extension.Message
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface OpenAIApi {
    @RetryOnError
    @Headers("Content-Type: application/json", "Authorization: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")
    @POST("v1/chat/completions")
    suspend fun generateResponse(@Body requestBody: OpenAIRequestBody): OpenAIResponse
}

data class OpenAIRequestBody(
    val model: String = "gpt-3.5-turbo",
    val messages: SnapshotStateList<Message>,
    val max_tokens: Int = 1000,
    val n: Int = 1,
    val temperature: Double = 1.0
)

data class OpenAIResponse(
    val choices: List<MessageResponse>
)

data class MessageResponse(
    val message: Message
)
