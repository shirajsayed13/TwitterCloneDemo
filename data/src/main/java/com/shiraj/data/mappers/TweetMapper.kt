package com.shiraj.data.mappers

import com.shiraj.data.response.TweetResponse
import com.shiraj.domain.model.Tweet

typealias TweetMapperAlias = Mapper<TweetResponse.Data, Tweet>

object TweetMapper : TweetMapperAlias {
    override fun map(input: TweetResponse.Data) = with(input) {
        Tweet(
            createdAt = createdAt,
            id = id,
            publicMetrics = Tweet.PublicMetrics(
                likeCount = publicMetrics.likeCount,
                quoteCount = publicMetrics.quoteCount,
                replyCount = publicMetrics.replyCount,
                retweetCount = publicMetrics.retweetCount
            ),
            text = text,
            attachments = attachments?.mediaKeys?.let {
                Tweet.Attachments(
                    mediaKeys = it
                )
            }
        )
    }
}