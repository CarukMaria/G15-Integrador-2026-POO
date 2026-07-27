package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultas") // Se creará una tabla conectada a la tabla "servicios"
public class Consulta extends Servicio {

    private String diagnostico;
    private String tratamiento;

    // 1. Constructor vacío OBLIGATORIO por JPA
    public Consulta() {
    }

    // 2. Método de negocio que me pediste
    public void registrarConsulta(String diagnostico, String tratamiento) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    public Consulta(String nombre, double precio, int duracion,
                String diagnostico, String tratamiento) {

        super(nombre, precio, duracion);
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
    }

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        return mascota != null;
    }

    // 3. Getters y Setters
    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }
}