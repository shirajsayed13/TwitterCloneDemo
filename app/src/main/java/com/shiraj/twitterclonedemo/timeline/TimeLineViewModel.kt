package com.shiraj.twitterclonedemo.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.model.TweetMediaModel
import com.shiraj.domain.usecase.TweetsUseCase
import com.shiraj.twitterclonedemo.login.LoginActivity.Companion.userProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val tweetsUseCase: TweetsUseCase
): ViewModel() {

    fun fetchTweets(): Flow<PagingData<Pair<List<Tweet>, List<TweetMediaModel>?>>> {
       return tweetsUseCase
            .fetch(userProfile?.userId!!)
            .cachedIn(viewModelScope)
    }
}