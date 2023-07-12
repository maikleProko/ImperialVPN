package com.banestudio.imperialvpn.ui.home.buttonvpn

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

class ProgressView(private val view: View) {
    private var currentScale = 0f

    init {
        view.scaleX = 0f
        view.scaleY = 0f
    }

    fun setProgress(value: Float, duration: Int) {
        view.scaleX = 1f
        view.scaleY = 1f
        val animation = ScaleAnimation(
            currentScale, value,
            currentScale, value,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.fillAfter = true
        animation.duration = duration.toLong()
        view.startAnimation(animation)
        currentScale = value
    }
}