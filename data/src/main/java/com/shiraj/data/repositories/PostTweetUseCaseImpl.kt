package com.shiraj.data.repositories

import com.shiraj.data.BuildConfig
import com.shiraj.data.api.ApiService
import com.shiraj.domain.usecase.PostTweetUseCase
import javax.inject.Inject

class PostTweetUseCaseImpl @Inject constructor(
    private val apiService: ApiService
) : PostTweetUseCase {

    override suspend fun postTweet(tweet: String) {
        apiService.postTweets(
            BuildConfig.BEARER_TOKEN,
            tweet
        )
    }
}