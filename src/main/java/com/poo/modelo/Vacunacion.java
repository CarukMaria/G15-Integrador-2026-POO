package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacunaciones")
public class Vacunacion extends Servicio {

    // Es el único atributo necesario para mantener la relación 1 a 1 que mencionás
    @ManyToOne
    @JoinColumn(name = "vacuna_id", nullable = false)
    private Vacuna vacuna;

    // Constructor vacío por defecto que requiere JPA (protegido)
    protected Vacunacion() {
        super();
    }

    public Vacunacion(String nombre, double precio, int duracion, Vacuna vacuna) {
        super(nombre, precio, duracion);
        setVacuna(vacuna); // Pasa por la validación del setter
    }

    // --- MÉTODOS DE NEGOCIO ---

    // Este método debe quedarse sí o sí porque es abstracto en la clase Servicio
    @Override
    public boolean validarRequisitos(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("Se requiere una mascota para registrar la vacunación.");
        }
        return true;
    }

    // --- GETTERS Y SETTERS PROTEGIDOS ---
    
    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        if (vacuna == null) {
            throw new IllegalArgumentException("La vacunación debe tener un objeto Vacuna asociado.");
        }
        this.vacuna = vacuna;
    }
}