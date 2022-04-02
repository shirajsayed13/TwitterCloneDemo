package com.shiraj.data.mappers

import com.shiraj.data.response.TweetResponse
import com.shiraj.domain.model.TweetMediaModel


typealias TweetIncludeMapperAlias = Mapper<TweetResponse.IncludeMedia.TweetMedia, TweetMediaModel>

object IncludeMapper : TweetIncludeMapperAlias {
    override fun map(input: TweetResponse.IncludeMedia.TweetMedia) = with(input) {
        TweetMediaModel(
            width = width,
            type = type,
            mediaKey = mediaKey,
            height = height,
            url = url
        )
    }
}