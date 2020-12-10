package com.therapdroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionUtil {

    private static ConnectionUtil instance = null;

    private ConnectionUtil() {}

    public static ConnectionUtil getInstance () {
        if (instance == null) instance = new ConnectionUtil();
        return instance;
    }

    public boolean isConnected (Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info.isConnected();
    }

    public void showMessage (Context context) {
        final String MESSAGE = "Check your internet connection and try again";
        Toast.makeText(context, MESSAGE, Toast.LENGTH_SHORT).show();
    }

}
