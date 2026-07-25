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
import java.util.List;

@Entity
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVeterinario;

    private String matricula;
    private String nombre;
    private String apellido;

    // Mapeo para una lista de elementos simples/enums utilizando JPA (@ElementCollection)
    @ElementCollection
    @CollectionTable(name = "veterinario_especialidades", joinColumns = @JoinColumn(name = "idVeterinario"))
    @Column(name = "especialidad")
    private List<String> especialidades; // O List<Especialidad> si prefieres crear un Enum o Entidad separada


    //VAMOS A HACER UN ENUM enum Especialidad
/*@Enumerated(EnumType.STRING)
@ElementCollection
private List<Especialidad> especialidades;*/


    // 1. Constructor vacío OBLIGATORIO por especificación de JPA
    public Veterinario() {
        this.especialidades = new ArrayList<>();
    }

    // 2. Constructor con parámetros para inicializar el objeto
    public Veterinario(String matricula, String nombre, String apellido) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidades = new ArrayList<>();
    }

    // 3. Getters y Setters
    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    // 4. Métodos de dominio (Modelo Rico) para manejar la colección de forma segura
    public void agregarEspecialidad(String especialidad) {
        if (especialidad != null && !especialidad.trim().isEmpty()) {
            this.especialidades.add(especialidad);
        }
    }

    @Override
    public String toString() {
        return "Dr. " + nombre + " " + apellido + " (Matrícula: " + matricula + ")";
    }
}