package com.banestudio.imperialvpn.ui.home.buttonvpn;
import android.view.View;
import android.widget.TextView;

import com.banestudio.imperialvpn.R;

public class TextNotificationView {
    private TextView textView;
    public TextNotificationView(TextView textView) {
        this.textView = textView;
    }

    public void setText(String text) {
        textView.setText(text);
    }

}
