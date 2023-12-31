package com.banestudio.imperialvpn.ui.home.buttonvpn

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.ak.ui.CountryCodePicker
import com.banestudio.imperialvpn.R
import com.banestudio.imperialvpn.core.VpnConnection
import com.banestudio.imperialvpn.databinding.ButtonVpnBinding

class ButtonVpnView(
    private val buttonVpnBinding: ButtonVpnBinding,
    private val progressView: ProgressView,
    private val textNotificationView: TextNotificationView,
    private val context: Context,
    private val activity: Activity,
    private val countryCodePicker: CountryCodePicker,
    private val fragment: Fragment
) {
    private var fadeIn: Animation? = null
    private var fadeOut: Animation? = null
    private val vpnConnection: VpnConnection = VpnConnection(context, activity)

    init {
        loadAnimations()
        setOnTouchListener()
    }

    private fun loadAnimations() {
        val fadeInit = AnimationUtils.loadAnimation(
            context, R.anim.fade_init
        )
        fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeInit)
    }

    private fun setOnTouchListener() {
        buttonVpnBinding.circlebuttonvpnalpha.setOnTouchListener { view: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeIn)
            } else if (event.action == MotionEvent.ACTION_UP) {
                buttonVpnBinding.circlebuttonvpnalpha.startAnimation(fadeOut)
                startVPN()
            }
            true
        }
    }

    fun startVPN() {
        vpnConnection.checkRequirementsAndStart(
            fragment, countryCodePicker.selectedCountryName()
        )
    }

    private fun isAnyVpnConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networks = arrayOfNulls<Network>(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networks = connectivityManager.allNetworks
        }
        if (networks == null) {
            return false
        }
        for (network in networks) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_VPN) &&
                    !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun setProgressVPNConnection(progress: Float, duration: Int, text: String) {
        progressView.setProgress(progress, duration)
        textNotificationView.setText(text)
    }

    fun updateProgressVPNConnection() {
        if (isAnyVpnConnected(context)) vpnConnection.setStatusVPN(2)
        val progress: Float
        val duration: Int
        val text: String

        when (vpnConnection.getStatusVPN()) {
            0 -> {
                progress = 0.2f
                duration = 800
                text = activity.getString(R.string.notification_find_servers)
            }
            1 -> {
                progress = 0.73f
                duration = 400
                text = activity.getString(R.string.notification_connecting) + vpnConnection.getServerCountry()
            }
            2 -> {
                progress = 1f
                duration = 800
                text = activity.getString(R.string.notification_connected) + vpnConnection.getServerCountry()
            }
            else -> {
                progress = 0f
                duration = 0
                text = activity.getString(R.string.notification_default)
            }
        }
        setProgressVPNConnection(progress, duration, text)
    }
}