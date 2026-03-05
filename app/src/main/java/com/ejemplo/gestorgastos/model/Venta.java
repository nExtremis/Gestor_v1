package com.ejemplo.gestorgastos.model;

import java.util.Date;

public class Venta {
    private int id;
    private int productoId;
    private int contactoId; // Nuevo campo
    private String nombreProducto;
    private String nombreContacto; // Nuevo campo
    private Date fecha;
    private double cantidad;
    private double precio;
    private boolean f;
    private String tipoPago;
    private String detalles;

    public Venta() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int id) { this.productoId = id; }

    public int getContactoId() { return contactoId; }
    public void setContactoId(int id) { this.contactoId = id; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombre) { this.nombreProducto = nombre; }

    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombre) { this.nombreContacto = nombre; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean isF() { return f; }
    public void setF(boolean f) { this.f = f; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }
}
