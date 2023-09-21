package com.tnmd.learningenglishapp.screens.chatgpt

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Thời gian chờ kết nối
        .readTimeout(30, TimeUnit.SECONDS)    // Thời gian chờ đọc dữ liệu
        .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian chờ ghi dữ liệu
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openAIApi: OpenAIApi = retrofit.create(OpenAIApi::class.java)
}