package com.shiraj.twitterclonedemo.timeline

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.shiraj.twitterclonedemo.R
import com.shiraj.twitterclonedemo.base.BaseFragment
import com.shiraj.twitterclonedemo.databinding.FragmentTimeLineBinding
import com.shiraj.twitterclonedemo.loadUrl
import com.shiraj.twitterclonedemo.login.LoginActivity.Companion.userProfile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class TimeLineFragment :
    BaseFragment<FragmentTimeLineBinding>(R.layout.fragment_time_line) {

    private val viewModel: TimeLineViewModel by viewModels()

    private val viewAdapter: TweetAdapter by lazy(LazyThreadSafetyMode.NONE) { TweetAdapter() }

    override fun bind(view: View) = FragmentTimeLineBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfile?.imageUrl?.let { binding.ivProfileImage.loadUrl(it) }
        setupRecyclerView()
        setupRetryButton()
        setupViewModel()
        navigateToPostTweetFragment()
    }

    private fun navigateToPostTweetFragment() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_timeLineFragment_to_postTweetFragment)
        }
    }

    private fun setupRetryButton() = with(binding.btnRetry) {
        setOnClickListener { viewAdapter.retry() }
    }

    private fun setupRecyclerView() = with(binding.rvTweet) {
        setHasFixedSize(true)
        this.adapter = viewAdapter.withLoadStateHeaderAndFooter(
            header = TweetStateAdapter { viewAdapter.retry() },
            footer = TweetStateAdapter { viewAdapter.retry() }
        )
        viewAdapter.addLoadStateListener { loadState ->
            val isEmptyList = loadState.refresh is LoadState.NotLoading &&
                    viewAdapter.itemCount == 0
            showEmptyList(isEmptyList)

            binding.rvTweet.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.pbItems.isVisible = loadState.source.refresh is LoadState.Loading
            binding.btnRetry.isVisible = loadState.source.refresh is LoadState.Error
        }
    }

    private fun showEmptyList(isEmptyList: Boolean) {
        binding.tvNoTweet.isVisible = isEmptyList
    }

    private fun setupViewModel() = with(viewModel) {
        lifecycleScope.launch {
            fetchTweets().collectLatest { pagingData ->
                binding.pbItems.isVisible = false
                viewAdapter.submitData(pagingData)
            }
        }
    }
}