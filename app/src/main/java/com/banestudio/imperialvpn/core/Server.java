package com.banestudio.imperialvpn.core;

public class Server {
    private String country;
    private String ovpn;
    private String ovpnUserName;
    private String ovpnUserPassword;
    private String ovpnVersion;

    public String getCountry() {
        return country;
    }
    public String getOvpn() {
        return ovpn;
    }
    public String getOvpnUserName() {
        return ovpnUserName;
    }
    public String getOvpnUserPassword() {
        return ovpnUserPassword;
    }

    public void set(String value) {
        String[] params = value.split("\\|");
        this.ovpnUserName = params[0];
        this.ovpnUserPassword = params[1];
        this.country = params[2];
        this.ovpn = this.country + ".ovpn";
        this.ovpnVersion = params[3];
    }

    public void initTestServer() {
        this.ovpnUserName = "freeopenvpn";
        this.ovpnUserPassword = "839987206";
        this.ovpn = "USA.ovpn";
        this.ovpnVersion = "-1";
    }

}
