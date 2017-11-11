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
 * Created by crisi on 11/09/2017.
 */


public class productos {
    private int id;
    private String descripcion;
    private int precio;
    private int stock;

    public productos() {
        this.id = 0;
        this.descripcion = null;
        this.precio= 0;
        this.stock= 0;
    }

    public int getid() {
        return id;
    }

    public String getdescripcion() {
        return descripcion;
    }


    public int getprecio() {
        return precio;
    }

    public int getstock(){
        return stock;
    }


    public void setid(int id) {
        this.id = id;
    }

    public void setdescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public void setprecio(int precio) {
        this.precio = precio;
    }

    public void setstock(int stock) {
        this.stock = stock;
    }

    public List<String[]> selecttodo(Connection conexion, Context contexto)  {
        List<String[]> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT id, descripcion, precio, stock FROM  productos");
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add(new String[]{resultado.getString("id"),resultado.getString("descripcion"),resultado.getString("precio"),resultado.getString("stock")});
            }
        }catch(SQLException ex){
            Toast.makeText(contexto, "Consulta erronéa"+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ob;
    }

    public List<String> selectdes(Connection conexion, Context contexto) {

        List<String> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT descripcion FROM  productos;");
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add(resultado.getString("descripcion"));
            }
        }catch(SQLException ex){
            Toast.makeText(contexto, "Consulta erronéa"+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return ob;
    }

    public void update(Connection conexion ) throws SQLException{
        try{
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("UPDATE productos SET stock= ? WHERE id = ?");
            consulta.setInt(1, this.getstock());
            consulta.setInt(2, this.getid());
            consulta.executeUpdate();
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
}
