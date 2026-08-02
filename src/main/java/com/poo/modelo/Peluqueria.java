package com.poo.modelo;

import jakarta.persistence.Entity;

@Entity
public class Peluqueria extends Servicio {

    // Constructor vacío por defecto que requiere JPA (protegido)
    protected Peluqueria() {
        super();
    }

    public Peluqueria(String nombre, double precio, int duracion) {
        super(nombre, precio, duracion);
    }

    // --- MÉTODOS DE NEGOCIO ---

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("Se requiere una mascota para el servicio de peluquería.");
        }

        //La peluquería estándar solo atiende perros y gatos.
        Especie especie = mascota.getEspecie();
        
        if (especie != Especie.PERRO && especie != Especie.GATO) {
            throw new IllegalArgumentException(
                "El servicio de peluquería solo está disponible para perros y gatos. " +
                "Especie ingresada: " + especie
            );
        }
        return true;
    }
}