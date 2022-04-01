package com.shiraj.domain.model

data class TweetResponseModel(
    val `data`: List<Tweet>,
    val meta: Meta,
    val includes: Tweet.Includes
)

data class Tweet(
    val createdAt: String,
    val id: String,
    val publicMetrics: PublicMetrics,
    val text: String,
    val attachments: Attachments
) {
    data class PublicMetrics(
        val likeCount: Int,
        val quoteCount: Int,
        val replyCount: Int,
        val retweetCount: Int
    )

    data class Attachments(
        val mediaKeys: List<String>
    )

    data class Includes(
        val width: Int,
        val type: String,
        val mediaKey: String,
        val height: Int,
        val url: String
    )
}

data class Meta(
    val newestId: String,
    val nextToken: String,
    val oldestId: String,
    val resultCount: Int
)
