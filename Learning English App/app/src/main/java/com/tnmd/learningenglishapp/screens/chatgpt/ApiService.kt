package com.tnmd.learningenglishapp.screens.chatgpt

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {
    private val rateLimitInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        // Kiểm tra xem response có lỗi 429 (Too Many Requests) hay không
        if (response.code == 429) {
            // Đợi một khoảng thời gian (ví dụ: 5 giây) và thử lại yêu cầu
            TimeUnit.SECONDS.sleep(20)
            return@Interceptor chain.proceed(request)
        }

        return@Interceptor response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(rateLimitInterceptor) // Thêm Interceptor để xử lý lỗi 429
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openAIApi: OpenAIApi = retrofit.create(OpenAIApi::class.java)
}