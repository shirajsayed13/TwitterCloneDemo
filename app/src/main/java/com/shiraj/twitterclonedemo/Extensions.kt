package com.shiraj.twitterclonedemo

import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.imageview.ShapeableImageView
import java.lang.String.format
import java.time.Instant
import java.util.*

fun Fragment.toast(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

internal fun ShapeableImageView.loadUrl(
    url: String,
    @DrawableRes placeholderResId: Int? = null,
    scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
) {
    val builder = Glide.with(this).load(url)
    if (null != placeholderResId) builder.placeholder(placeholderResId)
    builder.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            this@loadUrl.scaleType = scaleType
            return false
        }

    })
        .into(this)

}

fun getDateFromString(string: String?): String? {
    val timestamp: Instant?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        timestamp = Instant.parse(string)
        return Date.from(timestamp).getTimeAgo()
    }
    return null
}

fun Date.getTimeAgo(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val currentCalendar = Calendar.getInstance()

    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)

    return when {
        year < currentYear -> {
            val interval = currentYear - year
            "$interval${"yr"}"
        }
        month < currentMonth -> {
            val monthName = format(Locale.ENGLISH, "%tb", calendar)
            "$day $monthName"
        }
        day < currentDay -> {
            val interval = currentDay - day
            "$interval${"d"}"
        }
        hour < currentHour -> {
            val interval = currentHour - hour
            "$interval${"hr"}"
        }
        minute < currentMinute -> {
            val interval = currentMinute - minute
            "$interval${"min"}"
        }
        else -> {
            "a moment ago"
        }
    }
}