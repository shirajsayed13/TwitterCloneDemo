package com.shiraj.twitterclonedemo

import android.graphics.drawable.Drawable
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
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
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