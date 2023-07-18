package com.banestudio.imperialvpn.core

class Server {
    var country: String? = null

    var ovpn: String? = null
        private set
    var ovpnUserName: String? = null
        private set
    var ovpnUserPassword: String? = null
        private set
    private var ovpnVersion: String? = null
    fun set(value: String) {
        println(value)
        val params = value.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        ovpnUserName = params[0]
        ovpnUserPassword = params[1]
        country = params[2]
        ovpn = country + ".ovpn"
        ovpnVersion = params[3]
    }

    fun initTestServer() {
        ovpnUserName = "freeopenvpn"
        ovpnUserPassword = "839987206"
        ovpn = "USA.ovpn"
        ovpnVersion = "-1"
    }
}