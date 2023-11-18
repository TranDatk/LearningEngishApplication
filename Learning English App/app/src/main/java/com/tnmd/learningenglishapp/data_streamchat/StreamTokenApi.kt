package com.tnmd.learningenglishapp.data_streamchat

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
interface StreamTokenApi {
    @GET("token")
    suspend fun getToken(
        @Query("user_id") userId: String
    ): TokenResponse

    companion object {
        operator fun invoke(): StreamTokenApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.9:8080/")
                .build()
            return retrofit.create(StreamTokenApi::class.java)
        }
    }
}

// vào trong ip của mạng đang dùng để lấy ip
// dán vào trong code
// nếu chạy ra Server running on port 8080 là được và giữ nguyên để server chạy