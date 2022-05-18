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
        //checkConnetion();
        Log.d("inside", "oncreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw  new UnsupportedOperationException("Not yet implement");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //checkConnetion();
        handler.post(periodicUpdate);
        return START_STICKY;
    }

    public boolean isOnline(Context c){

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate,1*1000-SystemClock.elapsedRealtime()%1000);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.BroadcastStringForAction);
            broadcastIntent.putExtra("online_status",""+isOnline(Servicios.this));
            sendBroadcast(broadcastIntent);
        }
    };
/*
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
                }
                else if (nwInfo.getType() != ConnectivityManager.TYPE_WIFI){
                    Toast.makeText(this, "Sin conexion", Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                Log.e("Exception Connectivity", e.getMessage());
                System.out.println("Soy el cath");
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
