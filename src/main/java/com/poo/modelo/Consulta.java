package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "consultas") 
public class Consulta extends Servicio {

    private String diagnostico;
    private String tratamiento;

    // Constructor vacío requerido por JPA (protegido para evitar mal uso)
    protected Consulta() {
        super();
    }

    // Constructor 1: Para cuando se agenda la consulta (aún no hay diagnóstico)
    public Consulta(String nombre, double precio, int duracion) {
        super(nombre, precio, duracion);
    }

    // Constructor 2: Para cuando se necesita instanciar una consulta ya completada
    public Consulta(String nombre, double precio, int duracion, 
                    String diagnostico, String tratamiento) {
        
        super(nombre, precio, duracion);
        registrarConsulta(diagnostico, tratamiento); // Reutilizamos la lógica de negocio
    }


    // --- MÉTODOS DE NEGOCIO ---
    
    // Este método representa la acción real en el dominio (el acto médico)
    public void registrarConsulta(String diagnostico, String tratamiento) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("Para registrar la consulta, el diagnóstico es obligatorio.");
        }
        if (tratamiento == null || tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException("Para registrar la consulta, debe indicar un tratamiento o recomendaciones.");
        }
        
        this.diagnostico = diagnostico.trim();
        this.tratamiento = tratamiento.trim();
    }

    // Un método rico para consultar el estado interno
    public boolean estaCompletada() {
        return this.diagnostico != null && !this.diagnostico.trim().isEmpty();
    }

    @Override
    public boolean validarRequisitos(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("No se puede realizar una consulta sin una mascota asignada.");
        }
        return true; 
    }


    // --- GETTERS Y SETTERS ---
    
    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        if (diagnostico != null && diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("Si se ingresa un diagnóstico, no puede ser un texto en blanco.");
        }
        this.diagnostico = diagnostico != null ? diagnostico.trim() : null;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        if (tratamiento != null && tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException("Si se ingresa un tratamiento, no puede ser un texto en blanco.");
        }
        this.tratamiento = tratamiento != null ? tratamiento.trim() : null;
    }
}