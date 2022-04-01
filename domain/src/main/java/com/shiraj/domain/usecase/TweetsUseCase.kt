package com.shiraj.domain.usecase

import androidx.paging.PagingData
import com.shiraj.domain.model.Tweet
import kotlinx.coroutines.flow.Flow

interface TweetsUseCase {

    fun fetch(userId: Long): Flow<PagingData<Tweet>>
}