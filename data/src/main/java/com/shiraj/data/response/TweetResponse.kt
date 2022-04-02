package com.shiraj.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.IgnoredOnParcel
import java.util.*

@JsonClass(generateAdapter = true)
data class TweetResponse(
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "meta")
    val meta: Meta,
    @Json(name = "includes")
    val includes: IncludeMedia?
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
        val text: String,
        @Json(name = "attachments")
        val attachments: Attachments?
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

        @JsonClass(generateAdapter = true)
        data class Attachments(
            @Json(name = "media_keys")
            var mediaKeys: List<String>
        )
    }

    @JsonClass(generateAdapter = true)
    data class IncludeMedia(
        @Json(name = "media")
        var includeMedia: List<TweetMedia>
    ) {
        @JsonClass(generateAdapter = true)
        data class TweetMedia(
            @Json(name = "width")
            val width: Int?,
            @Json(name = "type")
            val type: String?,
            @Json(name = "media_key")
            val mediaKey: String?,
            @Json(name = "height")
            val height: Int?,
            @Json(name = "url")
            val url: String?
        )
    }

    @JsonClass(generateAdapter = true)
    data class Meta(
        @Json(name = "newest_id")
        val newestId: String,
        @Json(name = "next_token")
        val nextToken: String?,
        @Json(name = "previous_token")
        val previousToken: String?,
        @Json(name = "oldest_id")
        val oldestId: String,
        @Json(name = "result_count")
        val resultCount: Int
    )
}