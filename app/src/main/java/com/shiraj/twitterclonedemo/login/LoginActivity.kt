package com.shiraj.twitterclonedemo.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.shiraj.twitterclonedemo.Constants
import com.shiraj.twitterclonedemo.Constants.CONSUMER_KEY
import com.shiraj.twitterclonedemo.Constants.CONSUMER_SECRET
import com.shiraj.twitterclonedemo.Constants.OAUTH_TOKEN
import com.shiraj.twitterclonedemo.Constants.OAUTH_TOKEN_SECRET
import com.shiraj.twitterclonedemo.Constants.OAUTH_VERIFIER
import com.shiraj.twitterclonedemo.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var twitterDialog: Dialog
    private lateinit var twitter: Twitter
    private var accToken: AccessToken? = null
    private var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lifecycleScope.launch {
            val results = lifecycleScope.async { isLoggedIn() }
            val result = results.await()
        }

        findViewById<Button>(R.id.twitter_login_btn).setOnClickListener {
            getRequestToken()
        }
    }

    private fun getRequestToken() {
        lifecycleScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setIncludeEmailEnabled(true)

            twitter = TwitterFactory(builder.build()).instance

            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebViewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                Log.e("ERROR: ", e.toString())
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebViewDialog(url: String) {
        twitterDialog = Dialog(this)
        WebView(this).apply {
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            webViewClient = TwitterWebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
            twitterDialog.setContentView(this)
            twitterDialog.show()
        }
    }

    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(Constants.CALLBACK_URL)) {
                handleUrl(request?.url.toString())
                if (request?.url.toString().contains(Constants.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(Constants.CALLBACK_URL)) {
                handleUrl(url)

                if (url.contains(Constants.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        private fun handleUrl(url: String) {
            val oauthVerifier = Uri.parse(url).getQueryParameter(OAUTH_VERIFIER) ?: ""
            lifecycleScope.launch(Dispatchers.Main) {
                accToken =
                    withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
                getUserProfile()
            }
        }
    }

    suspend fun getUserProfile() {
        val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
        accessToken = accToken?.token ?: ""

        val sharedPreference = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreference.edit().putString(OAUTH_TOKEN, accToken?.token ?: "").apply()
        sharedPreference.edit().putString(OAUTH_TOKEN_SECRET, accToken?.tokenSecret ?: "").apply()
    }


    private suspend fun isLoggedIn(): Boolean {
        val sharedPreference = this.getPreferences(Context.MODE_PRIVATE)
        val accessToken = sharedPreference.getString(OAUTH_TOKEN, "")
        val accessTokenSecret = sharedPreference.getString(OAUTH_TOKEN_SECRET, "")

        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(CONSUMER_KEY)
            .setOAuthConsumerSecret(CONSUMER_SECRET)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
        twitter = TwitterFactory(builder.build()).instance
        return try {
            withContext(Dispatchers.IO) { twitter.verifyCredentials() }
            true
        } catch (e: Exception) {
            false
        }
    }
}