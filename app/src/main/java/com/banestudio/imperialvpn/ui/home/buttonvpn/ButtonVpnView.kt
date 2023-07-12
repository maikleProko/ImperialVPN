package com.banestudio.imperialvpn.ui.home.buttonvpn;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;

import com.banestudio.imperialvpn.R;
import com.banestudio.imperialvpn.core.VpnConnection;
import com.banestudio.imperialvpn.databinding.ButtonVpnBinding;
import com.ramotion.directselect.examples.advanced.AdvancedExampleCountryPickerBox;

public class ButtonVpnView {
    private Animation fadeIn;
    private Animation fadeOut;
    private final ProgressView progressView;
    private final TextNotificationView textNotificationView;
    private final VpnConnection vpnConnection;
    private final Context context;
    private final ButtonVpnBinding buttonVpnBinding;
    private final AdvancedExampleCountryPickerBox advancedExampleCountryPickerBox;
    private final Fragment fragment;
    private final Activity activity;

    public ButtonVpnView(
        ButtonVpnBinding buttonVpnBinding,
        ProgressView progressView,
        TextNotificationView textNotificationView,
        AdvancedExampleCountryPickerBox advancedExampleCountryPickerBox,
        Context context,
        Activity activity,
        Fragment fragment
    ) {
        this.buttonVpnBinding = buttonVpnBinding;
        this.progressView = progressView;
        this.textNotificationView = textNotificationView;
        this.advancedExampleCountryPickerBox = advancedExampleCountryPickerBox;
        this.context = context;
        this.activity = activity;
        this.fragment = fragment;
        vpnConnection = new VpnConnection(context, activity);
        loadAnimations();
        setOnTouchListener();
    }

    private void loadAnimations() {
        Animation fadeInit = AnimationUtils.loadAnimation(context, R.anim.fade_init);
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeInit);
    }

    private void setOnTouchListener() {
        buttonVpnBinding.circlebuttonvpnalpha.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeIn);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeOut);
                startVPN();
            }
            return true;
        });
    }

    public void startVPN() {
        vpnConnection.checkRequirementsAndStart(fragment,
                advancedExampleCountryPickerBox.getText());
    }

    private boolean isAnyVpnConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        Network[] networks = new Network[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networks = connectivityManager.getAllNetworks();
        }
        if (networks == null) {
            return false;
        }
        for (Network network : networks) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) &&
                        !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setProgressVPNConnection(float progress, int duration, String text) {
        progressView.setProgress(progress, duration);
        textNotificationView.setText(text);
    }

    public void updateProgressVPNConnection() {
        switch (vpnConnection.getStatusVPN()) {
            case -1:
                setProgressVPNConnection(0, 0, activity.getString(R.string.notification_default));
                break;
            case 0:
                setProgressVPNConnection(0.3f, 800, activity.getString(R.string.notification_find_servers));
                break;
            case 1:
                setProgressVPNConnection(0.8f, 4000, activity.getString(R.string.notification_connecting) + vpnConnection.getServerCountry());
                if (this.isAnyVpnConnected(context))
                    vpnConnection.setStatusVPN(2);
                break;
            case 2:
                setProgressVPNConnection(1f, 800, activity.getString(R.string.notification_connected) + vpnConnection.getServerCountry());
                break;
            default:
                break;
        }
    }

    public int getStatusVPN() {
        return vpnConnection.getStatusVPN();
    }
}
