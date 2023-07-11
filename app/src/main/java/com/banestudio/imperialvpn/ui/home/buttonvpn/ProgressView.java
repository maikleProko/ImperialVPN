package com.banestudio.imperialvpn.ui.home.buttonvpn;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ProgressView {
    private float currentScale = 0;
    private View view;

    public ProgressView(View view) {
        this.view = view;
        view.setScaleX(0);
        view.setScaleY(0);
    }

    public void setProgress(float value, int duration) {
        view.setScaleX(1);
        view.setScaleY(1);
        ScaleAnimation animation = new ScaleAnimation(
                currentScale, value,
                currentScale, value,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        animation.setFillAfter(true);
        animation.setDuration(duration);
        view.startAnimation(animation);
        currentScale = value;
    }

}
