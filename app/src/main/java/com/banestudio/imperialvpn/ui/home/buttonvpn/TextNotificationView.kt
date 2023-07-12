package com.banestudio.imperialvpn.ui.home.buttonvpn

import android.widget.TextView

class TextNotificationView(private val textView: TextView) {
    fun setText(text: String?) {
        textView.text = text
    }
}