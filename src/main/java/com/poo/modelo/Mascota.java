package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMascota;

    private String numeroFicha;
    private String nombre;
    private String raza;
    private LocalDate fechaNacimiento;

    // Guarda el Enum como un String en la base de datos (más seguro que un número)
    @Enumerated(EnumType.STRING)
    private Especie especie;

    // --- Relaciones estructurales deducidas de tus métodos ---
    // (Te darán error temporal hasta que creemos las clases HistorialMedico y Turno)
    
    /* @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente dueño;
    
    @OneToMany(mappedBy = "mascota")
    private List<HistorialMedico> historiales = new ArrayList<>();

    @OneToMany(mappedBy = "mascota")
    private List<Turno> turnos = new ArrayList<>();
    */

    // 1. Constructor vacío OBLIGATORIO por especificación de JPA
    public Mascota() {
    }

    // 2. Constructor con parámetros
    public Mascota(String numeroFicha, String nombre, String raza, LocalDate fechaNacimiento, Especie especie) {
        this.numeroFicha = numeroFicha;
        this.nombre = nombre;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.especie = especie;
    }

    // 3. Getters y Setters
    public Long getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Long idMascota) {
        this.idMascota = idMascota;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    // 4. Métodos de Comportamiento (Modelo Rico)

    // Método que devolverá la lista de historiales médicos
    public List<?> obtenerHistorialMedico() {
        // return historiales; (Descomentar cuando exista la clase)
        return new ArrayList<>(); 
    }

    // Valida si un nuevo turno se choca con los que ya tiene la mascota
    public boolean tieneTurnoSolapado(LocalDateTime fechaHoraNuevo, int duracionMinutos) {
        LocalDateTime finNuevo = fechaHoraNuevo.plusMinutes(duracionMinutos);

        /* Lógica real (descomentar cuando Turno exista):
        for (Turno turnoExistente : turnos) {
            LocalDateTime inicioExistente = turnoExistente.getFechaHora();
            LocalDateTime finExistente = inicioExistente.plusMinutes(turnoExistente.getDuracion());

            // Si el nuevo inicio es antes de que termine el existente Y 
            // el nuevo fin es después de que empiece el existente, HAY SOLAPAMIENTO.
            if (fechaHoraNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                return true; 
            }
        }
        */
        return false; 
    }

    @Override
    public String toString() {
        return nombre + " (" + especie + " - " + raza + ")";
    }
}