package com.shiraj.twitterclonedemo.post

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shiraj.twitterclonedemo.R

class PostTweetFragment : Fragment() {

    companion object {
        fun newInstance() = PostTweetFragment()
    }

    private lateinit var viewModel: PostTweetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_tweet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostTweetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}