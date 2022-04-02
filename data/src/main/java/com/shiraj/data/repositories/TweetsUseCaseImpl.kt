package com.shiraj.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shiraj.data.api.ApiService
import com.shiraj.data.mappers.TweetIncludeMapperAlias
import com.shiraj.data.mappers.TweetMapperAlias
import com.shiraj.data.paging.TweetPagingSource
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.model.TweetMediaModel
import com.shiraj.domain.usecase.TweetsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TweetsUseCaseImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapperAlias: TweetMapperAlias,
    private val includeMapper: TweetIncludeMapperAlias,
) : TweetsUseCase {
    override fun fetch(userId: Long): Flow<PagingData<Pair<List<Tweet>, List<TweetMediaModel>?>>> = Pager(
        pagingSourceFactory = {
            TweetPagingSource(
                apiService,
                mapperAlias,
                includeMapper,
                userId
            )
        },
        config = PagingConfig(
            pageSize = 1,
            prefetchDistance = 1
        )
    ).flow
}