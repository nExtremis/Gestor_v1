package com.ejemplo.gestorgastos.model;

import java.util.Date;

public class Gasto {
    private int id;
    private Date fecha;
    private double cantidad;
    private double precio;
    private String detalles;
    private boolean esProducto; // Nuevo campo

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public boolean isEsProducto() { return esProducto; }
    public void setEsProducto(boolean esProducto) { this.esProducto = esProducto; }
}
