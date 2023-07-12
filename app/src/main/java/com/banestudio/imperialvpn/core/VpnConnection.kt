package com.banestudio.imperialvpn.core

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.VpnService
import android.os.RemoteException
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import de.blinkt.openvpn.OpenVpnApi
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class VpnConnection(context: Context, activity: Activity) {
    private var statusVPN = -1
    private val server = Server()
    private val context: Context
    private val activity: Activity
    private val requestText = "Germany"

    init {
        server.initTestServer()
        this.context = context
        this.activity = activity
        Log.d(ContentValues.TAG, context.toString())
    }

    private val isConnectedToInternet: Boolean
        private get() {
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
        Log.d(ContentValues.TAG, config.toString())
        return config.toString()
    }

    private fun requestPermission(fragment: Fragment) {
        fragment.startActivityForResult(VpnService.prepare(context), 1)
    }

    fun checkRequirementsAndStart(fragment: Fragment /*, String requestText*/) {
        //this.requestText = requestText;
        if (statusVPN == -1 && isConnectedToInternet) {
            val intent = VpnService.prepare(context)
            if (intent != null) requestPermission(fragment) else start()
        }
    }

    private fun start() {
        setStatusVPN(0)
        sendRequest("http://192.168.1.151/get?country=" + requestText)
    }

    private fun sendRequest(url: String?) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            { serverParams: String -> enableVPN(serverParams) }) { error: VolleyError? -> }
        queue.add(stringRequest)
    }

    private fun enableVPN(serverParams: String) {
        try {
            server.set(serverParams)
            val bufferedReader = createBufferedReader()
            val config = createConfig(bufferedReader)
            OpenVpnApi.startVpn(
                context,
                config,
                server.country,
                server.ovpnUserName,
                server.ovpnUserPassword
            )
            setStatusVPN(1)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun getServerCountry(): String? {
        return server.country;
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