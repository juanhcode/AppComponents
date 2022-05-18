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
import android.widget.Toast;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkConnetion();
        return START_STICKY;
    }

    public void checkConnetion()  {
        for (int i = 0; i < 10; i++){
            System.out.println("Hola soy un for" + i);
        }
            try{
                ConnectivityManager conexionManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nwInfo = conexionManager.getNetworkInfo(conexionManager.getActiveNetwork());
                if (nwInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Toast.makeText(this, "Con conexion", Toast.LENGTH_SHORT).show();
                    Intent miIntent2 = new Intent(Servicios.this,Home.class);
                    startActivity(miIntent2);
                }else {
                    Intent miIntent3 = new Intent(Servicios.this,SinConexion.class);
                    startActivity(miIntent3);
                    Toast.makeText(this, "Sin Conexion", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                Log.e("Exception Connectivity", e.getMessage());
            }
        }

        /*
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

    }*/
}
