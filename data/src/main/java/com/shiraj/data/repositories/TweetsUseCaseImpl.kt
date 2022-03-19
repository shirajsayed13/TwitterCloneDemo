package com.shiraj.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shiraj.data.api.ApiService
import com.shiraj.data.mappers.TweetMapperAlias
import com.shiraj.data.paging.TweetPagingSource
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.TweetsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TweetsUseCaseImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapperAlias: TweetMapperAlias
) : TweetsUseCase {
    override fun fetch(): Flow<PagingData<Tweet>> = Pager(
        pagingSourceFactory = {
            TweetPagingSource(
                apiService,
                mapperAlias
            )
        },
        config = PagingConfig(
            pageSize = 10
        )
    ).flow
}