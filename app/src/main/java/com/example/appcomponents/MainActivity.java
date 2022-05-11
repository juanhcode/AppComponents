package com.example.appcomponents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private String url ="https://run.mocky.io/v3/7dd9aaa6-ee63-443d-8c1f-a60945f17478";
    private RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn = findViewById(R.id.buttonLogin);
        rq = Volley.newRequestQueue(this);
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
    //Consumir Api y validar
    public void auth(EditText email, EditText pass){
        JsonArrayRequest requerimiento = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try{
                        JSONObject obj = new JSONObject(response.get(i).toString());
                        if(textoEmail.equals(obj.getString("email")) && textoPass.equals(obj.getString("password"))){
                            System.out.println("SI EXISTE EL USUARIO");
                            String correo = obj.getString("email");
                            String email = obj.getString("password");
                            System.out.println(correo + email);
                        }else{
                            email.setText("");
                            pass.setText("");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(requerimiento);
    }
}