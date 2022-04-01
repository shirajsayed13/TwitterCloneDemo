package com.shiraj.data.mappers

import com.shiraj.data.response.TweetResponse
import com.shiraj.domain.model.Tweet


typealias TweetIncludeMapperAlias = Mapper<TweetResponse.Includes, Tweet.Includes>

object IncludeMapper : TweetIncludeMapperAlias {
    override fun map(input: TweetResponse.Includes) = with(input) {
        Tweet.Includes(
            width = width,
            type = type,
            mediaKey = mediaKey,
            height = height,
            url = url
        )
    }
}