package com.shiraj.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shiraj.data.BuildConfig
import com.shiraj.data.Constants
import com.shiraj.data.api.ApiService
import com.shiraj.data.mappers.TweetIncludeMapperAlias
import com.shiraj.data.mappers.TweetMapperAlias
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.model.TweetMediaModel
import retrofit2.HttpException
import java.io.IOException

class TweetPagingSource(
    private val apiService: ApiService,
    private val mapper: TweetMapperAlias,
    private val includeMapper: TweetIncludeMapperAlias,
    private val userId: Long
) : PagingSource<String, Pair<List<Tweet>, List<TweetMediaModel>?>>() {

    override fun getRefreshKey(state: PagingState<String, Pair<List<Tweet>, List<TweetMediaModel>?>>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Pair<List<Tweet>, List<TweetMediaModel>?>> {
        return try {
            val response = apiService.getTweets(
                BuildConfig.BEARER_TOKEN,
                4695994044,
                Constants.EXCLUDE,
                10,
                Constants.TWEET_FIELDS,
                Constants.EXPANSIONS,
                Constants.MEDIA_FIELDS
            )
            val tweets = response.data.map(mapper::map)
            val includeRes = response.includes?.includeMedia?.map(includeMapper::map)
            val pairResult = Pair(tweets, includeRes)
            val meta = response.meta
            LoadResult.Page(
                data = listOf(pairResult),
                prevKey = if (!meta.previousToken.isNullOrEmpty()) meta.previousToken else null,
                nextKey = if (!meta.nextToken.isNullOrEmpty()) meta.nextToken else null,
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean = true

}
