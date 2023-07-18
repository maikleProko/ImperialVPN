package com.banestudio.imperialvpn.core
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.VpnService
import android.os.RemoteException
import androidx.fragment.app.Fragment
import de.blinkt.openvpn.OpenVpnApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class VpnConnection(context: Context, activity: Activity) {
    private var statusVPN = -1
    private val server = Server()
    private val context: Context
    private val activity: Activity
    private var requestUrl = ""
    private val coroutineScopeREST: CoroutineScope = CoroutineScope(Job())

    init {
        server.initTestServer()
        this.context = context
        this.activity = activity
    }

    private val isConnectedToInternet: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            return nInfo != null && nInfo.isConnectedOrConnecting
        }

    @Throws(IOException::class)
    private fun createBufferedReader(): BufferedReader {
        return BufferedReader(InputStreamReader(activity.assets.open(server.ovpn!!)))
    }

    @Throws(IOException::class)
    private fun createConfig(bufferedReader: BufferedReader): String {
        val config = StringBuilder()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            config.append(line).append("\n")
        }
        bufferedReader.readLine()
        return config.toString()
    }

    private fun requestPermission(fragment: Fragment) {
        fragment.startActivityForResult(VpnService.prepare(context), 1)
    }

    fun checkRequirementsAndStart(fragment: Fragment, requestText: String) {
        this.requestUrl = "http://192.168.1.151/get?country=$requestText"
        if (statusVPN == -1 && isConnectedToInternet) {
            val intent = VpnService.prepare(context)
            if (intent != null) requestPermission(fragment) else start()
        }
    }

    private fun start() {
        setStatusVPN(0)
        coroutineScopeREST.launch {
            HttpHelper.getStringREST(requestUrl)?.let {
                enableVPN(it)
            }
        }
    }

    private fun enableVPN(serverParams: String) {
        try {
            setStatusVPN(1)
            server.set(serverParams)
            OpenVpnApi.startVpn(
                context,
                createConfig(createBufferedReader()),
                server.country,
                server.ovpnUserName,
                server.ovpnUserPassword
            )
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun getServerCountry(): String? {
        return server.country
    }

    fun getStatusVPN(): Int {
        return statusVPN
    }

    fun setStatusVPN(value: Int) {
        if (value > -2 && value < 3) {
            statusVPN = value
        }
    }
}