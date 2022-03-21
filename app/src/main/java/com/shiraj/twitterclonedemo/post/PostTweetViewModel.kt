package com.shiraj.twitterclonedemo.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shiraj.domain.usecase.PostTweetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostTweetViewModel @Inject constructor(
    private val postTweetUseCase: PostTweetUseCase
): ViewModel() {

    fun postTweet(tweet: String) {
        viewModelScope.launch {
            postTweetUseCase.postTweet(tweet)
        }
    }
}