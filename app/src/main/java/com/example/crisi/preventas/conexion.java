package com.example.crisi.preventas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.*;
/**
 * Created by crisi on 11/09/2017.
 */

public class conexion {

    Connection conn=null;

    @SuppressLint("NewApi")
    public Connection conectar() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/preventas", "softpreventas", "preventas2017");
        } catch (SQLException | ClassNotFoundException e) {
          //  Toast.makeText(contexto, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("errores", e.getMessage());
        }
        return conn;
    }

    public void cerrar() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

}
