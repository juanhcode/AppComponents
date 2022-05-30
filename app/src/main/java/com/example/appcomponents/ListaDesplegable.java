package com.example.appcomponents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListaDesplegable  extends AppCompatActivity {

    private EditText et1, et2;
    private CheckBox check1, check2;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = (EditText)findViewById(R.id.txt_valor1);
        et2 = (EditText)findViewById(R.id.txt_valor2);
        check1 = (CheckBox)findViewById(R.id.check_Suma);
        check2 = (CheckBox)findViewById(R.id.check_Resta);
        tv1 = (TextView)findViewById(R.id.tv_resultado);
    }

    //Este método es la función del botón
    public void Calcular(View view){
        String valor1_String = et1.getText().toString();
        String valor2_String = et2.getText().toString();

        int valor1_int = Integer.parseInt(valor1_String);
        int valor2_int = Integer.parseInt(valor2_String);

        String reultado = "";
        if(check1.isChecked() == true){
            int suma = valor1_int + valor2_int;
            reultado = " La suma es: " + suma + " / ";
        }
        if(check2.isChecked() == true){
            int resta = valor1_int - valor2_int;
            reultado = reultado + " La Resta es: " + resta;
        }
        tv1.setText(reultado);
    }
}
