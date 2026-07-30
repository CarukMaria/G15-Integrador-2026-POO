package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vacunaciones")
public class Vacunacion extends Servicio {

    private String nombreVacuna;
    private String laboratorio;

    @ManyToOne
    @JoinColumn(name = "vacuna_id")
    private Vacuna vacuna;

    public Vacunacion() {
    }


    public Vacunacion(String nombre,
                  double precio,
                  int duracion,
                  Vacuna vacuna,
                  String laboratorio) {

        super(nombre, precio, duracion);

            this.vacuna = vacuna;
            this.nombreVacuna = vacuna.getNombre();
        this.laboratorio = laboratorio;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
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