package com.example.appcomponents;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

public class Servicios extends Service {


    public void onCreate(){
        super.onCreate();
        checkConnetion();
        Log.d("inside", "oncreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void checkConnetion()  {
        // Opcion 3  por medio de Runnable
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    // Connectivy manager
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    boolean isWifiConn = false;
                    boolean isMobileConn = false;
                    NetworkInfo nwInfo = cm.getNetworkInfo(cm.getActiveNetwork());
                    // runOnUiThread <<--posible solucion para desde qui acceder a la UI
                    if (nwInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        //Toast.makeText(getApplicationContext(), "I am Wifi", Toast.LENGTH_SHORT).show();
                        Log.d("", " I am Wifi ");
                        System.out.println("I am wifi");
                        System.out.println("soy el hilo" + i);
                        isWifiConn = true;
                    } else if (nwInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isMobileConn = true;
                        //Toast.makeText(this, "I am Mobile", Toast.LENGTH_SHORT).show();
                        Log.d("", " I am Mobile");
                        System.out.println("I am mobile");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
