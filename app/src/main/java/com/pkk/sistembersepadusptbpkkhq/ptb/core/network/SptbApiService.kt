package com.pkk.sistembersepadusptbpkkhq.core.network

import com.pkk.sistembersepadusptbpkkhq.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface SptbApiService {

    // GET endpoints
    @GET("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun getData(
        @Query("action") action: String = "getData",
        @Query("role") role: String = "",
        @Query("userName") userName: String = "",
        @Query("v") version: String = "",
    ): DataResponse

    @GET("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun getStats(
        @Query("action") action: String = "getStats",
        @Query("role") role: String = "",
        @Query("userName") userName: String = "",
    ): StatsResponse

    @GET("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun checkAuth(
        @Query("action") action: String = "checkAuth",
        @Query("email") email: String = "",
    ): AuthCheckResponse

    @GET("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun getChangelog(
        @Query("action") action: String = "getChangelog",
    ): List<Map<String, String>>

    @GET("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun getQueueData(
        @Query("action") action: String = "getQueueData",
        @Query("email") email: String = "",
    ): QueueResponse

    // POST endpoints
    @POST("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun postAction(@Body request: ApiRequest): ApiResponse

    // AI Extraction
    @POST("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun extractAI(@Body request: ApiRequest): AIExtractionResponse

    // Auth
    @POST("/macros/s/AKfycbxnutXT6atF4vgzqIXTB1z1oZdEFEfnLEg2nFYDfkIwIdPtpHTBfQqFlkphXzUm5V-y/exec")
    suspend fun postAuth(@Body request: ApiRequest): AuthCheckResponse

    companion object {
        fun create(): SptbApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://script.google.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SptbApiService::class.java)
        }
    }
}
