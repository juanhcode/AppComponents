package com.example.appcomponents;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.appcomponents.databinding.ActivityLoginBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private String textoEmail = "";
    private String textoPass ="";
    private String url ="https://run.mocky.io/v3/9e16a21c-60f5-40ae-af28-de84bd220dbd";
    private RequestQueue rq;
    //Variables de Google
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    boolean isWifiConn = false;
    boolean isMobileConn = false;
    ActivityLoginBinding binding;
    public static final String BroadcastStringForAction = "checkinternet";
    private IntentFilter mIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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

        //setContentView(R.layout.activity_login);
        rq = Volley.newRequestQueue(this);
        //Identificador de google
        signin = findViewById(R.id.sign_in_button);
        //Evento iniciar Google
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.sign_in_button:
                        signIn(); //Metodo de iniciar sesion
                        break;
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Evento consumir API
        Button btn = findViewById(R.id.buttonLogin);
        //iniciarServicio();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.TextEmail);
                EditText pass = findViewById(R.id.TextPassword);
                textoEmail = email.getText().toString();
                textoPass = pass.getText().toString();
                auth(email,pass);
            }
        });


        //startService(new Intent(MainActivity.this, Servicios.class));

        /*
        // Connectivy manager
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = cm.getNetworkInfo(cm.getActiveNetwork());
        if(nwInfo.getType() == ConnectivityManager.TYPE_WIFI){
            Toast.makeText(this, "I am Wifi", Toast.LENGTH_SHORT).show();
            isWifiConn = true;
        }else if (nwInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            isMobileConn = true;
            Toast.makeText(this, "I am Mobile", Toast.LENGTH_SHORT).show();
        }*/
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


    // ========================== aqui se verifica la conexion a internet ==========================
    public void Set_Visibility_ON(){
        binding.notConnected.setVisibility(View.GONE);
        binding.buttonLogin.setVisibility(View.VISIBLE);
        binding.getRoot().setBackgroundColor(Color.WHITE);
    }

    public void Set_Visibility_OFF(){
        binding.notConnected.setVisibility(View.VISIBLE);
        binding.buttonLogin.setVisibility(View.GONE);
        binding.signInButton.setVisibility(View.GONE);
        binding.getRoot().setBackgroundColor(Color.RED);
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



    public void iniciarServicio(){
        Intent service = new Intent(this, Servicios.class);
        startService(service);
    }

    //Iniciar sesion
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Intent miIntent = new Intent(MainActivity.this,Home.class);
            startActivity(miIntent);
        } catch (ApiException e) {
            System.out.println("-----------------------------error-----------------------------------------");
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    //API VOLLEY
    public int econtrar(JSONArray respuesta){
        int id = 0;
        for (int i=0;i<respuesta.length();i++){
            try{
                JSONObject obj = new JSONObject(respuesta.get(i).toString());
                System.out.println("este es el obj: " + obj);
                if(textoEmail.equals(obj.getString("email")) && textoPass.equals(obj.getString("password"))){
                    id = Integer.parseInt(obj.getString("id"));
                    System.out.println("parseint" + id);
                    return id;
                }
                else {
                    id = -1;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return id;
    }

    //Consumir Api y validar
    public void auth(EditText email, EditText pass){
        Servicios servicio = new Servicios();
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("Entre en el if");
                    System.out.println("este es el response: " + response.getJSONObject(econtrar(response) - 1));
                    loginClick();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Datos Erroneos", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(requerimiento);
    }

    public void loginClick(){
        Intent miIntent = new Intent(MainActivity.this,Home.class);
        startActivity(miIntent);
    }


}