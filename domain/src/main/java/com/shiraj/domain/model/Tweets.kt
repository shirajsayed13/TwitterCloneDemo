package com.shiraj.domain.model


data class Tweet(
    val createdAt: String,
    val id: String,
    val publicMetrics: PublicMetrics,
    val text: String
) {
    data class PublicMetrics(
        val likeCount: Int,
        val quoteCount: Int,
        val replyCount: Int,
        val retweetCount: Int
    )
}
