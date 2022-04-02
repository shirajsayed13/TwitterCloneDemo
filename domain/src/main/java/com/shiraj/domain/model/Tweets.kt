package com.shiraj.domain.model

data class Tweet(
    val createdAt: String,
    val id: String,
    val publicMetrics: PublicMetrics,
    val text: String,
    val attachments: Attachments?
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
}

data class TweetMediaModel(
    val width: Int?,
    val type: String?,
    val mediaKey: String?,
    val height: Int?,
    val url: String?
)
