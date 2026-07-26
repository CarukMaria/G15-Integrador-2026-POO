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
    private int duracion; // Duración expresada en minutos (según diagrama)

    // 1. Constructor vacío OBLIGATORIO por especificación de JPA
    public Servicio() {
    }

    // 2. Constructor con parámetros para inicializar el objeto
    public Servicio(String nombre, double precio, int duracion) {
        this.nombre = nombre;
        this.precio = precio;
        this.duracion = duracion;
    }

    // 3. Getters y Setters
    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    // 4. Comportamiento de dominio exigido por la regla de oro (Modelo Rico)
    public boolean validarRequisitos(Mascota mascota) {
        // Lógica de negocio de ejemplo basada en el dominio:
        // Por ejemplo, verificar que la mascota exista y cumpla condiciones básicas para el servicio
        if (mascota == null) {
            return false;
        }
        
        // Aquí puedes agregar validaciones propias de tu veterinaria (ej: edad mínima, especie permitida, etc.)
        return true; 
    }

    // Método opcional para actualizar precio respetando encapsulamiento
    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio > 0) {
            this.precio = nuevoPrecio;
        }
    }

    @Override
    public String toString() {
        return nombre + " (" + duracion + " mins) - $" + precio;
    }
}
