package com.poo.modelo;

import jakarta.persistence.Entity;

@Entity
public class Peluqueria extends Servicio {

    // Constructor vacío por defecto que requiere JPA
    public Peluqueria() {
        super();
    }

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        return mascota != null;
    }
}