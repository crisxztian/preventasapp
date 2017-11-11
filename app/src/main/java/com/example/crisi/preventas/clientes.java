package com.example.crisi.preventas;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by crisi on 16/09/2017.
 */

public class clientes {
    private int id;
    private String nombre;
    private String documento;
    private String direccion;

    public clientes() {
        this.id = 0;
        this.nombre = null;
        this.direccion = null;
        this.documento= null;
    }

    public String getdocumento() {
        return documento;
    }

    public void setdocumento(String documento) {
        this.documento = documento;
    }

    public int getid() {
        return id;
    }

    public String getnombre() {
        return nombre;
    }

    public String getdireccion() {
        return direccion;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setnombre(String nombre) {
        this.nombre = nombre;
    }

    public void setdireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String[]> selectdoc(Connection conexion, Context contexto) {
        List<String[]> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT id, documento FROM clientes;");
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add( new String[]{resultado.getString("id"), resultado.getString("documento")});
            }
        }catch(SQLException ex){
            Toast.makeText(contexto, "Consulta erronéa"+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ob;
    }

    public void insert(Connection conexion) throws SQLException {
        try {
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("INSERT INTO clientes" + "(documento, nombre, direccion) VALUES(?, ?, ?)");
            consulta.setString(1, this.getdocumento());
            consulta.setString(2, this.getnombre());
            consulta.setString(3, this.getdireccion());
            consulta.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public int selectid(Connection conexion) throws SQLException {
        int sec=0;
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT id FROM  clientes" + " WHERE documento = ? " );
            consulta.setString(1, this.documento);
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                sec=resultado.getInt("id");
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }

        return sec;
    }

}
