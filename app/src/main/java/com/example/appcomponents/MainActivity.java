package com.example.appcomponents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String textoEmail = "";
    private String textoPass ="";
    private String url ="https://run.mocky.io/v3/9e16a21c-60f5-40ae-af28-de84bd220dbd";
    private RequestQueue rq;

    //Variables de Google
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    System.out.println("este es el response: " + response.getJSONObject(econtrar(response)-1));
                    LoginClick();

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

    public void LoginClick(){
        Intent miIntent = new Intent(MainActivity.this,Home.class);
        startActivity(miIntent);
    }

}