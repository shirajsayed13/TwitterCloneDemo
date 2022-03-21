package com.shiraj.data.repositories

import com.shiraj.data.api.ApiService
import com.shiraj.domain.usecase.PostTweetUseCase
import javax.inject.Inject


private const val BEARER_TOKEN =
    "Bearer AAAAAAAAAAAAAAAAAAAAAD%2BaaAEAAAAAA5CaRS4uLn5e91%2BXaZzFK4JL0VU%3Dx2rh8Bj7DlKVFJRIjvj890h9WjUrzujar3f1ry9zjNq86MLOPQ"

class PostTweetUseCaseImpl @Inject constructor(
    private val apiService: ApiService
) : PostTweetUseCase {

    override suspend fun postTweet(tweet: String) {
        apiService.postTweets(
            BEARER_TOKEN,
            tweet
        )
    }
}