package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "servicios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;

    private String nombre;
    private double precio;
    private int duracion; 

    // Constructor vacío requerido por JPA (Lo hacemos protected)
    protected Servicio() {
    }

    // Constructor con parámetros (Pasa por las validaciones de los setters)
    public Servicio(String nombre, double precio, int duracion) {
        setNombre(nombre);
        setPrecio(precio);
        setDuracion(duracion);
    }
    
    // ---- Reglas de negocio ----
    
    // Este método abstracto es oro puro para el modelo rico (Polimorfismo)
    public abstract boolean validarRequisitos(Mascota mascota);

    // Método de negocio explícito para actualizar precios (Falla ruidosamente si hay un error)
    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio < 0) {
            throw new IllegalArgumentException("El precio de un servicio no puede ser negativo.");
        }
        this.precio = nuevoPrecio;
    }

    @Override
    public String toString() {
        return nombre + " (" + duracion + " mins) - $" + precio;
    }

    // ---- Getters y Setters PROTEGIDOS ----
    
    public Long getIdServicio() {
        return idServicio;
    }
    
    // Nota: Se elimina setIdServicio() porque la BD (JPA) es la única responsable de asignar IDs.

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        actualizarPrecio(precio); // Reutilizamos la lógica de negocio para no repetir código
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración del servicio debe ser mayor a 0 minutos.");
        }
        this.duracion = duracion;
    }
}