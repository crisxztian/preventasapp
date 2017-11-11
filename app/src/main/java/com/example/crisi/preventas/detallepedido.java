package com.example.crisi.preventas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by crisi on 11/09/2017.
 */

public class detallepedido {
    private int id;
    private int cantidad;
    private int pedidos_id;
    private int productos_id;

    public detallepedido() {
        this.id = 0;
        this.cantidad = 0;
        this.pedidos_id = 0;
        this.productos_id = 0;

    }

    public int getid() {
        return id;
    }

    public int getcantidad() {
        return cantidad;
    }

    public int getpedidos_id() {
        return pedidos_id;
    }

    public int getproductos_id() {
        return productos_id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setcantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setpedidos_id(int pedidos_id) {
        this.pedidos_id = pedidos_id;
    }

    public void setproductos_id(int productos_id) {
        this.productos_id = productos_id;
    }

    public void insert(Connection conexion) throws SQLException {
        try {
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("INSERT INTO detallepedido" + "(cantidad, pedidos_id, productos_id) VALUES(?, ?, ?)");
            consulta.setInt(1, this.getcantidad());
            consulta.setInt(2, this.getpedidos_id());
            consulta.setInt(3, this.getproductos_id());
            consulta.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public List<String[]> selectdetalles(Connection conexion) throws SQLException {
        List<String[]> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT productos.id,productos.descripcion, productos.precio, detallepedido.cantidad FROM  detallepedido,productos,pedidos where pedidos.id=detallepedido.pedidos_id and productos.id=detallepedido.productos_id and detallepedido.pedidos_id = ?");
            consulta.setInt(1, this.pedidos_id);
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add(new String[]{resultado.getString("productos.id"),resultado.getString("productos.descripcion"),resultado.getString("productos.precio"),resultado.getString("detallepedido.cantidad")});
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return ob;
    }

    public List<String[]> selectcancelar(Connection conexion) throws SQLException {
        List<String[]> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT detallepedido.cantidad, detallepedido.productos_id, productos.stock FROM detallepedido,productos WHERE detallepedido.productos_id=productos.id and detallepedido.pedidos_id= ?");
            consulta.setInt(1, this.pedidos_id);
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add(new String[]{resultado.getString("detallepedido.cantidad"),resultado.getString("detallepedido.productos_id"),resultado.getString("productos.stock")});
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return ob;
    }
}
