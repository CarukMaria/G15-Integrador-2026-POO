package com.poo.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacunas")
public class Vacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVacuna;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String enfermedad;

    // Periodicidad en meses: cada cuántos meses se debe volver a aplicar.
    @Column(nullable = false)
    private int periodicidad;

    // Constructor requerido por JPA.
    public Vacuna() {
    }

    public Vacuna(String nombre, String enfermedad, int periodicidad) {
        setNombre(nombre);
        setEnfermedad(enfermedad);
        setPeriodicidad(periodicidad);
    }

    @Override
    public String toString() {
        return this.nombre; 
    }

    // --- MÉTODO DE NEGOCIO ---

    public boolean estaVigente(
            LocalDate fechaUltimaAplicacion,
            LocalDate fechaTurno) {

        if (fechaUltimaAplicacion == null || fechaTurno == null) {
            return false;
        }

        LocalDate fechaVencimiento =
                fechaUltimaAplicacion.plusMonths(periodicidad);

        return fechaTurno.isBefore(fechaVencimiento);
    }

    // --- VALIDACIONES DEL MODELO ---

    private String validarCadenaNoVacia(
            String valor,
            String nombreCampo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "El campo " + nombreCampo +
                " no puede ser nulo o estar vacío."
            );
        }

        return valor.trim();
    }

    // --- GETTERS Y SETTERS ---

    public Long getIdVacuna() {
        return idVacuna;
    }

    public void setIdVacuna(Long idVacuna) {
        this.idVacuna = idVacuna;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre =
                validarCadenaNoVacia(nombre, "Nombre");
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad =
                validarCadenaNoVacia(enfermedad, "Enfermedad");
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {

        if (periodicidad <= 0) {
            throw new IllegalArgumentException(
                "La periodicidad debe ser mayor a 0 meses."
            );
        }

        this.periodicidad = periodicidad;
    }
}