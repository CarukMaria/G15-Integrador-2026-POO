package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultas")
public class Consulta extends Servicio {

    // Constructor vacío requerido por JPA
    protected Consulta() {
        super();
    }

    // Constructor para registrar la consulta como servicio del catálogo
    public Consulta(String nombre, double precio, int duracion) {
        super(nombre, precio, duracion);
    }

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException(
                "No se puede realizar una consulta sin una mascota asignada."
            );
        }

        return true;
    }
}