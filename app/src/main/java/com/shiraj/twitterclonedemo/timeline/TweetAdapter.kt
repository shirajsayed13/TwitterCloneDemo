package com.shiraj.twitterclonedemo.timeline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shiraj.domain.model.Tweet
import com.shiraj.twitterclonedemo.databinding.ItemTweetBinding
import com.shiraj.twitterclonedemo.loadUrl
import com.shiraj.twitterclonedemo.login.LoginActivity.Companion.userProfile

class TweetAdapter : PagingDataAdapter<Tweet, TweetAdapter.ViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class ViewHolder(
        private val binding: ItemTweetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Tweet) = with(binding) {
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
        private val COMPARATOR = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem == newItem
        }
    }
}