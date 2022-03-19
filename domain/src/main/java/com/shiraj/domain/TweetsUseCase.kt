package com.shiraj.domain

import androidx.paging.PagingData
import com.shiraj.domain.model.Tweet
import kotlinx.coroutines.flow.Flow

interface TweetsUseCase {

    fun fetch(): Flow<PagingData<Tweet>>
}