package com.shiraj.data.api

import com.shiraj.data.response.TweetResponse
import retrofit2.http.*

/**
 * Retrofit API Service
 */
interface ApiService {

    @GET("2/users/{id}/tweets")
    suspend fun getTweets(
        @Path("id") userId: Long,
        @Query("exclude") exclude: String,
        @Query("max_results") maxResults: Int,
        @Query("tweet.fields") tweetFields: String,
        @Query("expansions") expansions: String,
        @Query("media.fields") mediaFields: String,
    ): TweetResponse

    @POST("2/tweets")
    suspend fun postTweets(
        @Header("Authorization") header: String,
        @Query("text") text: String
    )

}