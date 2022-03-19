package com.shiraj.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shiraj.data.api.ApiService
import com.shiraj.data.mappers.TweetMapperAlias
import com.shiraj.domain.model.Tweet
import retrofit2.HttpException
import java.io.IOException

private const val BEARER_TOKEN =
    "Bearer AAAAAAAAAAAAAAAAAAAAAD%2BaaAEAAAAAA5CaRS4uLn5e91%2BXaZzFK4JL0VU%3Dx2rh8Bj7DlKVFJRIjvj890h9WjUrzujar3f1ry9zjNq86MLOPQ"

class TweetPagingSource(
    private val apiService: ApiService,
    private val mapper: TweetMapperAlias
) : PagingSource<String, Tweet>() {

    override fun getRefreshKey(state: PagingState<String, Tweet>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Tweet> {
        return try {
            val response = apiService.getTweets(
                BEARER_TOKEN,
                "replies,retweets",
                10,
                "created_at,public_metrics"
            )
            val tweets = response.data.map(mapper::map)
            LoadResult.Page(
                data = tweets,
                prevKey = "",
                nextKey = response.meta.nextToken
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override val keyReuseSupported: Boolean = true

}
