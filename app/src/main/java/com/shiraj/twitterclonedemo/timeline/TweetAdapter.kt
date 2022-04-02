package com.shiraj.twitterclonedemo.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shiraj.domain.model.Tweet
import com.shiraj.domain.model.TweetMediaModel
import com.shiraj.twitterclonedemo.databinding.ItemTweetBinding
import com.shiraj.twitterclonedemo.getDateFromString
import com.shiraj.twitterclonedemo.loadUrl
import com.shiraj.twitterclonedemo.login.LoginActivity.Companion.userProfile

class TweetAdapter :
    PagingDataAdapter<Pair<List<Tweet>, List<TweetMediaModel>?>, TweetAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class ViewHolder(
        private val binding: ItemTweetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tweetPair: Pair<List<Tweet>, List<TweetMediaModel>?>) = with(binding) {
            val user = tweetPair.first[bindingAdapterPosition]
            val tweetMedia = tweetPair.second
            if (user.attachments != null) {
                user.attachments!!.mediaKeys.forEach { mediaKey ->
                    tweetMedia?.forEach { media ->
                        if (mediaKey == media.mediaKey) {
                            ivBodyImage.visibility = View.VISIBLE
                            media.url?.let { url -> ivBodyImage.loadUrl(url) }
                        } else {
                            ivBodyImage.visibility = View.GONE
                        }
                    }
                }
            }
            val dateFromString = "  • ${getDateFromString(user.createdAt)}"
            tvTimeStamp.text = dateFromString
            tvBodyText.text = user.text
            tvReply.text = user.publicMetrics.replyCount.toString()
            tvRetweet.text = user.publicMetrics.retweetCount.toString()
            tvFav.text = user.publicMetrics.likeCount.toString()
            tvUserName.text = userProfile?.name
            val userHandle = "@".plus("${userProfile?.userName}")
            tvUserHandle.text = userHandle
            userProfile?.imageUrl?.let { ivProfileImage.loadUrl(it) }
        }
    }

    companion object {
        private val COMPARATOR =
            object : DiffUtil.ItemCallback<Pair<List<Tweet>, List<TweetMediaModel>?>>() {
                override fun areItemsTheSame(
                    oldItem: Pair<List<Tweet>, List<TweetMediaModel>?>,
                    newItem: Pair<List<Tweet>, List<TweetMediaModel>?>
                ): Boolean =
                    oldItem == newItem

                override fun areContentsTheSame(
                    oldItem: Pair<List<Tweet>, List<TweetMediaModel>?>,
                    newItem: Pair<List<Tweet>, List<TweetMediaModel>?>
                ): Boolean =
                    oldItem == newItem
            }
    }
}