package com.shiraj.twitterclonedemo.login

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.shiraj.data.response.UserProfile
import com.shiraj.twitterclonedemo.Constants
import com.shiraj.twitterclonedemo.Constants.CONSUMER_KEY
import com.shiraj.twitterclonedemo.Constants.CONSUMER_SECRET
import com.shiraj.twitterclonedemo.Constants.OAUTH_TOKEN
import com.shiraj.twitterclonedemo.Constants.OAUTH_TOKEN_SECRET
import com.shiraj.twitterclonedemo.Constants.OAUTH_VERIFIER
import com.shiraj.twitterclonedemo.MainActivity
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
    private var accessToken: AccessToken? = null
    companion object {
        var userProfile: UserProfile? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lifecycleScope.launch {
            val results = lifecycleScope.async { isLoggedIn() }
            val result = results.await()
            if (result) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                findViewById<Button>(R.id.twitter_login_btn).setOnClickListener {
                    getRequestToken()
                }
            }
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
                accessToken =
                    withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
                getUserProfile()
            }
        }
    }

    suspend fun getUserProfile() {
        val user = withContext(Dispatchers.IO) { twitter.verifyCredentials() }
        val sharedPreference = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreference.edit().putString("name", user.name).apply()
        sharedPreference.edit().putString("username", user.screenName).apply()
        sharedPreference.edit().putString("image_url", user.profileImageURLHttps).apply()
        sharedPreference.edit()
            .putString(OAUTH_TOKEN, accessToken?.token ?: "")
            .apply()
        sharedPreference.edit()
            .putString(OAUTH_TOKEN_SECRET, accessToken?.tokenSecret ?: "")
            .apply()
    }


    private suspend fun isLoggedIn(): Boolean {
        val sharedPreference = this.getPreferences(Context.MODE_PRIVATE)
        val accessToken = sharedPreference.getString(OAUTH_TOKEN, "")
        val accessTokenSecret = sharedPreference.getString(OAUTH_TOKEN_SECRET, "")
        val name = sharedPreference.getString("name", "")
        val userName = sharedPreference.getString("username", "")
        val imageUrl = sharedPreference.getString("image_url", "")

        userProfile = UserProfile(name, userName, imageUrl)

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