package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Vacunacion extends Servicio {

    @ManyToOne
    @JoinColumn(name = "id_vacuna")
    private Vacuna vacuna;

    // Constructor vacío obligatorio para JPA
    public Vacunacion() {
        super();
    }

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        return verificarPeriodicidad(mascota);
    }

    // Método de negocio solicitado
    public boolean verificarPeriodicidad(Mascota m) {
        // Lógica de negocio para verificar la periodicidad de la vacuna en la mascota
        // (Por ejemplo, consultando el historial de aplicaciones de la mascota)
        return true; 
    }

    // Getters y Setters
    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }
}