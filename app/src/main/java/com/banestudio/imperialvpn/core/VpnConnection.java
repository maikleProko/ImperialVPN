package com.banestudio.imperialvpn.core;
import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.VpnService;
import android.os.RemoteException;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import de.blinkt.openvpn.OpenVpnApi;

public class VpnConnection{

    private int statusVPN = -1;
    private final Server server = new Server();
    private final Context context;
    private final Activity activity;
    private String requestText = "Auto";
    public VpnConnection(Context context, Activity activity) {
        server.initTestServer();
        this.context = context;
        this.activity = activity;
        Log.d(TAG, context.toString());
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        return nInfo != null && nInfo.isConnectedOrConnecting();
    }

    private BufferedReader createBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(activity.getAssets().open(server.getOvpn())));
    }

    private String createConfig(BufferedReader bufferedReader) throws IOException {
        StringBuilder config = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            config.append(line).append("\n");
        }
        bufferedReader.readLine();
        Log.d(TAG, config.toString());
        return config.toString();
    }

    private void requestPermission(Fragment fragment) {
        fragment.startActivityForResult(VpnService.prepare(context), 1);
    }

    public void checkRequirementsAndStart(Fragment fragment, String requestText) {
        this.requestText = requestText;
        if (statusVPN == -1 && isConnectedToInternet()) {
            Intent intent = VpnService.prepare(context);
            if (intent != null)
                requestPermission(fragment);
            else
                start();
        }
    }

    public void start() {
        setStatusVPN(0);
        sendRequest("http://192.168.1.151/get?country="+this.requestText);
    }

    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::enableVPN, error -> {

                });
        queue.add(stringRequest);
    }

    private void enableVPN(String serverParams) {
        try {
            server.set(serverParams);
            BufferedReader bufferedReader = createBufferedReader();
            String config = createConfig(bufferedReader);
            OpenVpnApi.startVpn(
                context,
                config,
                server.getCountry(),
                server.getOvpnUserName(),
                server.getOvpnUserPassword()
            );
            setStatusVPN(1);

        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getStatusVPN() {
        return statusVPN;
    }

    public void setStatusVPN(int value) {
        if (value > -2 && value < 3) {
            statusVPN = value;
        }
    }

    public String getServerCountry() {
        return server.getCountry();
    }

}
