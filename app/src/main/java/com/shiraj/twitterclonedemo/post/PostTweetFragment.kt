package com.shiraj.twitterclonedemo.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.shiraj.twitterclonedemo.R
import com.shiraj.twitterclonedemo.base.BaseFragment
import com.shiraj.twitterclonedemo.databinding.FragmentPostTweetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostTweetFragment : BaseFragment<FragmentPostTweetBinding>(R.layout.fragment_post_tweet) {

    private val viewModel: PostTweetViewModel by viewModels()

    override fun bind(view: View) = FragmentPostTweetBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val tweetText = binding.editText.text.toString()
        binding.button.setOnClickListener {
            //viewModel.postTweet(tweetText)
        }
    }

}