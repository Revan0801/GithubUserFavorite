package com.example.githubuserfavorite.api

import com.example.githubuserfavorite.BuildConfig.BASE_URL
import com.example.githubuserfavorite.api.ApiToken.GITHUB_TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    fun getApiService(): ApiService {

        val client = OkHttpClient.Builder()
            .addInterceptor{ chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", GITHUB_TOKEN)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}