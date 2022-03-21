package com.shiraj.domain.usecase

interface PostTweetUseCase {

    suspend fun postTweet(tweet: String)
}