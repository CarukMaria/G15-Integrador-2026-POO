package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "guarderias") // Tabla propia conectada a "servicios"
public class Guarderia extends Servicio {

    private int cupoMaximo;

    // 1. Constructor vacío requerido por JPA (protegido para evitar mal uso)
    protected Guarderia() {
        super();
    }

    // 2. Constructor completo para asegurar que nazca en un estado válido
    public Guarderia(String nombre, double precio, int duracion, int cupoMaximo) {
        super(nombre, precio, duracion);
        setCupoMaximo(cupoMaximo); // Llamamos al setter para que pase por la validación
    }

    // --- MÉTODOS DE NEGOCIO ---

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La guardería requiere una mascota válida para registrar el ingreso.");
        }
        
        // El modelo rico permite escalar reglas fácilmente. Por ejemplo, si en el futuro
        // deciden que no aceptan animales exóticos en la guardería, lo pondrías acá:
        // if (mascota.getEspecie().toString().equals("EXOTICO")) return false;
        
        return true;
    }

    // Responde lógicamente si todavía hay lugar físico
    public boolean hayCupoDisponible(int mascotasActuales) {
        if (mascotasActuales < 0) {
            throw new IllegalArgumentException("La cantidad de mascotas actuales en la guardería no puede ser negativa.");
        }
        return mascotasActuales < this.cupoMaximo;
    }
    
    // Comportamiento inteligente: la clase calcula y expone su propio estado
    public int calcularLugaresDisponibles(int mascotasActuales) {
        if (mascotasActuales < 0) {
            throw new IllegalArgumentException("La cantidad de mascotas actuales no puede ser negativa.");
        }
        
        int disponibles = this.cupoMaximo - mascotasActuales;
        return disponibles > 0 ? disponibles : 0; // Evita devolver negativos si hubo un sobrecupo por error de BD
    }

    // --- GETTERS Y SETTERS PROTEGIDOS ---

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        if (cupoMaximo <= 0) {
            throw new IllegalArgumentException("El cupo máximo de una guardería debe ser mayor a cero.");
        }
        this.cupoMaximo = cupoMaximo;
    }
}