package com.poo.modelo;
//
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

    // Periodicidad en meses (cada cuántos meses se debe volver a aplicar)
    @Column(nullable = false)
    private int periodicidad;

    // 1. Constructor vacío requerido por JPA
    public Vacuna() {
    }

    // 2. Constructor con parámetros
    public Vacuna(String nombre, String enfermedad, int periodicidad) {
        this.nombre = nombre;
        this.enfermedad = enfermedad;
        this.periodicidad = periodicidad;
    }

    @Override
    public String toString() {
        return nombre;
    }
    // --- MÉTODO DE NEGOCIO ---
    
    // Verifica si la vacuna sigue haciendo efecto hoy, basado en cuándo se aplicó
    public boolean estaVigente(LocalDate fechaUltimaAplicacion) {
        if (fechaUltimaAplicacion == null) {
            return false; // Si nunca se aplicó, no está vigente
        }
        
        // Sumamos los meses de periodicidad a la fecha en que se la aplicó
        LocalDate fechaVencimiento = fechaUltimaAplicacion.plusMonths(periodicidad);
        
        // Comprobamos si la fecha de hoy es ANTES de la fecha de vencimiento
        return LocalDate.now().isBefore(fechaVencimiento);
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
        this.nombre = nombre;
    }

    public String getEnfermedad() {
        return enfermedad;
    }

    public void setEnfermedad(String enfermedad) {
        this.enfermedad = enfermedad;
    }

    public int getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(int periodicidad) {
        this.periodicidad = periodicidad;
    }
}