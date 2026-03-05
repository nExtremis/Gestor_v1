package com.ejemplo.gestorgastos.model;

public class Producto {
    private int id;
    private String nombre;
    private double precioCosto;
    private double precioVentaMenor;
    private double precioVentaMayor;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(double precioCosto) { this.precioCosto = precioCosto; }

    public double getPrecioVentaMenor() { return precioVentaMenor; }
    public void setPrecioVentaMenor(double precioVentaMenor) { this.precioVentaMenor = precioVentaMenor; }

    public double getPrecioVentaMayor() { return precioVentaMayor; }
    public void setPrecioVentaMayor(double precioVentaMayor) { this.precioVentaMayor = precioVentaMayor; }
}
