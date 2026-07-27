package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "guarderias") // Tabla propia conectada a "servicios"
public class Guarderia extends Servicio {

    private int cupoMaximo;

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        return mascota != null;
    }
    
    // 1. Constructor vacío obligatorio por JPA
    public Guarderia() {
    }

    // 2. Método de negocio para verificar el cupo
    // (Retorna true si aún hay lugar. En una app real, acá consultarías a la BD)
    public boolean verificarCupo(int mascotasActuales) {
        return mascotasActuales < this.cupoMaximo;
    }

    // 3. Getters y Setters
    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
}