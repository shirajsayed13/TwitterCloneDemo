package com.shiraj.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shiraj.data.BuildConfig
import com.shiraj.data.api.ApiService
import com.shiraj.data.mappers.TweetMapperAlias
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.model.TweetResponseModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

class TweetPagingSource(
    private val apiService: ApiService,
    private val mapper: TweetMapperAlias,
    private val userId: Long
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
                BuildConfig.BEARER_TOKEN,
                userId,
                "replies,retweets",
                10,
                "created_at,public_metrics",
                "attachments.media_keys",
                "duration_ms,height,media_key,preview_image_url,public_metrics,type,url,width,alt_text"
            )
            val tweets = response.data.map(mapper::map)
            val includes = response.includes.url
            val mediaType = response.includes.mediaKey
            println("CHECK THIS tweets $tweets")
            println("CHECK THIS tweets includes $includes and mediaType $mediaType")
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
