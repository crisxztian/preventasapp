package com.example.crisi.preventas;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    conexion conex;
    EditText musuarioView;
    EditText mPasswordView;

    View mProgressView;
    View mLoginFormView;
    Button entrar;
    usuarios usu = new usuarios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        musuarioView = (EditText) findViewById(R.id.usuario);

        mPasswordView = (EditText) findViewById(R.id.password);

        entrar = (Button) findViewById(R.id.entrar);
        entrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               DoLogin doLogin = new DoLogin();
               doLogin.execute("");
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.progressBar);
        mProgressView.setVisibility(View.GONE);

        conex = new conexion();


    }


    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        String user = musuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        String[] key;
        @Override
        protected void onPreExecute() {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
            mProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            mProgressView.setVisibility(View.GONE);
            if (isSuccess) {
                Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
                Intent mainmenu = new Intent(getApplicationContext(), MainActivity.class);
                mainmenu.putExtra("idusuario", Integer.parseInt(key[0]));
                startActivity(mainmenu);
            }else{
                Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
                Log.i("errores",r);
            }

        }

        @Override
        protected String doInBackground(String... params) {

            if (user.trim().equals("") || password.trim().equals("")) {
                z = "Por favor ingrese usuario y contraseña";
            } else {
                try {
                    Connection con = conex.conectar();
                    if (con == null) {
                        z = "Error conectando a la base de datos";
                    } else {
                       key = usu.selectuser(con, user, LoginActivity.this);
                        if(key==null){
                            z = "Usuario inexistente";
                        }
                        else if (key[1].equals(password)) {
                            z = "Sesión iniciada";
                            isSuccess = true;
                        }else {
                            z = "Credenciales invalidas";
                            isSuccess = false;
                        }

                    }



                } catch (Exception ex) {
                    isSuccess = false;
                    Log.i("errores", key.toString());
                    z = ex.getMessage();
                }

            }
                return z;

        }

    }
}


