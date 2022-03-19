package com.shiraj.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TweetResponse(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "meta")
    val meta: Meta
) {
    @JsonClass(generateAdapter = true)
    data class Data(
        @Json(name = "created_at")
        val createdAt: String,
        @Json(name = "id")
        val id: String,
        @Json(name = "public_metrics")
        val publicMetrics: PublicMetrics,
        @Json(name = "text")
        val text: String
    ) {
        @JsonClass(generateAdapter = true)
        data class PublicMetrics(
            @Json(name = "like_count")
            val likeCount: Int,
            @Json(name = "quote_count")
            val quoteCount: Int,
            @Json(name = "reply_count")
            val replyCount: Int,
            @Json(name = "retweet_count")
            val retweetCount: Int
        )
    }

    @JsonClass(generateAdapter = true)
    data class Meta(
        @Json(name = "newest_id")
        val newestId: String,
        @Json(name = "next_token")
        val nextToken: String,
        @Json(name = "oldest_id")
        val oldestId: String,
        @Json(name = "result_count")
        val resultCount: Int
    )
}