
package com.example.ProyectoFinal.Entidades;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Carrusel {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombreArchivo;

    private String descripcion;

    private String rutaArchivo;

    public Carrusel() {}
    public Carrusel(int id, String descripcion, String nombreArchivo) {
    this.id = id;
    this.descripcion = descripcion;
    this.nombreArchivo = nombreArchivo;
}

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
}
