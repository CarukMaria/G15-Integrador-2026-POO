package com.poo.modelo;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeterinario;

    @Column(nullable = false, unique = true)
    private String matricula;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;

    // Mapeo para una lista de elementos simples/enums utilizando JPA (@ElementCollection)
    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "veterinario_especialidades",
        joinColumns = @JoinColumn(name = "idVeterinario")
    )
    @Column(name = "especialidad")
    private List<Especialidad> especialidades = new ArrayList<>();


    // 1. Constructor vacío protegido (requerido por JPA)
    protected Veterinario() {
    }

    // 2. Constructor con parámetros (pasa por validaciones)
    public Veterinario(String matricula, String nombre, String apellido) {
        setMatricula(matricula);
        setNombre(nombre);
        setApellido(apellido);
    }


    // ------ MÉTODOS DE NEGOCIO -----
    
    public void agregarEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new IllegalArgumentException("No se puede agregar una especialidad nula al veterinario.");
        }
        // Evitamos duplicados
        if (!this.especialidades.contains(especialidad)) {
            this.especialidades.add(especialidad);
        }
    }

    public void removerEspecialidad(Especialidad especialidad) {
        if (especialidad != null) {
            this.especialidades.remove(especialidad);
        }
    }

    // Comportamiento inteligente: El veterinario responde si tiene los conocimientos necesarios
    public boolean tieneEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            return false;
        }
        return this.especialidades.contains(especialidad);
    }


    // --- VALIDACIONES INTERNAS PRIVADAS ---

    private String validarCadenaNoVacia(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " del veterinario no puede estar vacío.");
        }
        return valor.trim();
    }


    // ----- GETTERS Y SETTERS PROTEGIDOS -----
    
    public Long getIdVeterinario() {
        return idVeterinario;
    }
    
    // Eliminamos setIdVeterinario para proteger la Primary Key

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = validarCadenaNoVacia(matricula, "Matrícula");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCadenaNoVacia(nombre, "Nombre");
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarCadenaNoVacia(apellido, "Apellido");
    }

    public List<Especialidad> getEspecialidades() {
        // Obliga a que cualquier cambio pase por agregarEspecialidad() o removerEspecialidad()
        return Collections.unmodifiableList(especialidades);
    }
    
    // Eliminamos setEspecialidades(List) para que no pisen la lista gestionada por JPA

    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellido + " (Matrícula: " + matricula + ")";
    }
}