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

public class pedidos {
    private int id;
    private String fecha;
    private int total;
    private int clientes_id;
    private int usuarios_id;

    public pedidos() {
        this.id = 0;
        this.fecha = null;
        this.total = 0;
        this.clientes_id = 0;
        this.usuarios_id = 0;

    }

    public int getid() {
        return id;
    }

    public String getfecha() {
        return fecha;
    }

    public int gettotal() {
        return total;
    }

    public int getclientes_id() {
        return clientes_id;
    }

    public int getusuarios_id() {
        return usuarios_id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setfecha(String fecha){
        this.fecha= fecha;
    }

    public void settotal(int total) {
        this.total = total;
    }

    public void setclientes_id(int clientes_id) {
        this.clientes_id = clientes_id;
    }

    public void setusuarios_id(int usuarios_id) {
        this.usuarios_id = usuarios_id;
    }

    public void insert(Connection conexion) throws SQLException {
        try {
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("INSERT INTO pedidos" + "(fecha, total, clientes_id, usuarios_id) VALUES(?, ?, ?, ?)");
            consulta.setString(1, this.getfecha());
            consulta.setInt(2, this.gettotal());
            consulta.setInt(3, this.getclientes_id());
            consulta.setInt(4, this.getusuarios_id());
            consulta.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public void updateestado(Connection conexion, String estado ) throws SQLException{
        try{
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("UPDATE pedidos" + " SET estado= ? WHERE id = ?");
            consulta.setString(1, estado);
            consulta.setInt(2, this.getid());
            consulta.executeUpdate();
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }

    public void update(Connection conexion ) throws SQLException{
        try{
            PreparedStatement consulta;
            consulta = conexion.prepareStatement("UPDATE pedidos" + " SET total= ? WHERE id = ?");
            consulta.setInt(1, this.gettotal());
            consulta.setInt(2, this.getid());
            consulta.executeUpdate();
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }

    public int selectid(Connection conexion) throws SQLException {
        int ob=0;
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT id FROM pedidos ORDER BY id DESC LIMIT 1");
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob=resultado.getInt("id");
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return ob;
    }

    public List<String[]> selectpendientes(Connection conexion) throws SQLException {
        List<String[]> ob = new ArrayList<>();
        try{
            PreparedStatement consulta = conexion.prepareStatement("SELECT pedidos.id, clientes.nombre, clientes.direccion, pedidos.total FROM  pedidos, clientes where clientes.id=pedidos.clientes_id and pedidos.estado='Pendiente' ORDER BY pedidos.fecha ASC");
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                ob.add(new String[]{resultado.getString("pedidos.id"),resultado.getString("clientes.nombre"),resultado.getString("clientes.direccion"),resultado.getString("pedidos.total")});
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return ob;
    }


}
