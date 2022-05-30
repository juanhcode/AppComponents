package com.example.appcomponents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.appcomponents.databinding.ActivityHomeBinding;

public class Home extends s {

    Button signOut;

    GoogleSignInClient mGoogleSignInClient;

    ActivityHomeBinding binding;
    public static final String BroadcastStringForAction = "checkinternet";
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this,Servicios.class);
        startService(serviceIntent);

        binding.notConnected.setVisibility(View.GONE);
        if (isOnline(getApplicationContext())){
            Set_Visibility_ON();
        }else {
            Set_Visibility_OFF();
        }
        signOut = findViewById(R.id.button);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    // ...
                    case R.id.button:
                        signOut();
                        break;
                    // ...
                }
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Home.this,"Signed out successfull",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        /*
        Intent service = new Intent(this, Servicios.class);
        startService(service);
         */
    }


    public BroadcastReceiver MyReceiever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastStringForAction)){
                if (intent.getStringExtra("online_status").equals("true"))
                {
                    Set_Visibility_ON();
                }else{
                    Set_Visibility_OFF();
                }
            }
        }
    };

    public boolean isOnline(Context c){

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }


    public void Set_Visibility_ON(){
        binding.notConnected.setVisibility(View.GONE);
    }

    public void Set_Visibility_OFF(){
        Intent miIntent = new Intent(Home.this,MainActivity.class);
        startActivity(miIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(MyReceiever,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiever);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiever,mIntentFilter);
    }

    // ========================== fin de verificacion a internet ==========================


}