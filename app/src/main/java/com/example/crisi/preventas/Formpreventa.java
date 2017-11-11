package com.example.crisi.preventas;

/**
 * Created by crisi on 26/09/2017.
 */

public class Formpreventa {

    private String descripcion;
    private int cantidad;
    private boolean check;


    public Formpreventa(String descripcion, int cantidad, boolean check) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.check = check;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
