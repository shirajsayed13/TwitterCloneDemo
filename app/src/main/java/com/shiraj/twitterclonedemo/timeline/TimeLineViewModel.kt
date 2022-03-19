package com.shiraj.twitterclonedemo.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.TweetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val tweetsUseCase: TweetsUseCase
): ViewModel() {

    fun fetchTweets(): Flow<PagingData<Tweet>> {
       return tweetsUseCase
            .fetch()
            .cachedIn(viewModelScope)
    }
}