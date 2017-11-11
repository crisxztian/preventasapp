package com.example.crisi.preventas;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by crisi on 11/09/2017.
 */

public class usuarios {
    private int id;
    private String documento;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String clave;
    private String privilegios;

    public usuarios() {
        this.documento=null;
        this.usuario = null;
        this.nombres = null;
        this.apellidos = null;
        this.clave = null;
        this.id = 0;
        this.privilegios = null;
    }



    public int getid() {
        return id;
    }


    public String getusuario() {
        return usuario;
    }

    public String getnombres() {
        return nombres;
    }

    public String getapellidos() {
        return apellidos;
    }

    public String getdocumento() {
        return documento ;
    }


    public String getclave() {
        return clave ;
    }

    public String getprivilegios(){
        return privilegios;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setusuario(String usuario) {
        this.usuario = usuario;
    }

    public void setnombres(String nombres) {
        this.nombres = nombres;
    }

    public void setapellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setdocumento(String documento) {
        this.documento = documento;
    }

    public void setclave(String clave) {
        this.clave = clave;
    }

    public void setprivilegios(String privilegios){
        this.privilegios=privilegios;
    }


    public void update(Connection con, Context contexto) {
        try{
            PreparedStatement consulta;
            consulta = con.prepareStatement("UPDATE usuarios" + " SET clave = ? WHERE usuario = ?");
            consulta.setString(1, this.getclave());
            consulta.setString(2, this.getusuario());
            consulta.executeUpdate();
        }catch(SQLException ex){

        }
    }

    public String[] selectuser(Connection con, String usuario, Context contexto) {
        String[] sec=null;
        try{
                PreparedStatement consulta = con.prepareStatement("SELECT id, clave FROM  usuarios" + " WHERE usuario = ? and privilegios='Basico'");
                consulta.setString(1, usuario);
                ResultSet resultado = consulta.executeQuery();
                while (resultado.next()) {
                    sec = new String[]{resultado.getString("id"), resultado.getString("clave")};
                }
        } catch (SQLException ex) {
            Toast.makeText(contexto, "Consulta erronéa"+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return sec;
    }

    public int selectid(Connection con, String usuario, Context contexto)  {
        int sec=0;
        try{
            PreparedStatement consulta = con.prepareStatement("SELECT id FROM  usuarios" + " WHERE usuario = '?' and privilegios='Basico'" );
            consulta.setString(1, usuario);
            ResultSet resultado = consulta.executeQuery();
            while(resultado.next()){
                sec=resultado.getInt("id");
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }

        return sec;
    }
}
