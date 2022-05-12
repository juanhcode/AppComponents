package com.example.appcomponents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String textoEmail = "";
    private String textoPass ="";
    private String url ="https://run.mocky.io/v3/9e16a21c-60f5-40ae-af28-de84bd220dbd";
    private RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rq = Volley.newRequestQueue(this);
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