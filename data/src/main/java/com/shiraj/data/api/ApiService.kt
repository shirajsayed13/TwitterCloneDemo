package com.shiraj.data.api

import com.shiraj.data.response.TweetResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Retrofit API Service
 */
interface ApiService {

    @GET("2/users/4695994044/tweets")
    suspend fun getTweets(
        @Header("Authorization") header: String,
        @Query("exclude") exclude: String,
        @Query("max_results") maxResults: Int,
        @Query("tweet.fields") tweetFields: String
    ): TweetResponse

}