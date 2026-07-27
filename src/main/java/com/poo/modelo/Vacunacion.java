package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacunaciones")
public class Vacunacion extends Servicio {

    private String nombreVacuna;
    private String laboratorio;


    public Vacunacion() {
    }


    public Vacunacion(String nombre, double precio, int duracion,
                      String nombreVacuna, String laboratorio) {

        super(nombre, precio, duracion);
        this.nombreVacuna = nombreVacuna;
        this.laboratorio = laboratorio;
    }


    public String getNombreVacuna() {
        return nombreVacuna;
    }


    public void setNombreVacuna(String nombreVacuna) {
        this.nombreVacuna = nombreVacuna;
    }


    public String getLaboratorio() {
        return laboratorio;
    }


    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }


    @Override
    public boolean validarRequisitos(Mascota mascota) {
        return mascota != null;
    }
}