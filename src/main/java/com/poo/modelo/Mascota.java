package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Esta columna guardará el id del dueño en la tabla de mascotas
    private Cliente cliente;

    private String numeroFicha;
    private String nombre;
    private String raza;
    private LocalDate fechaNacimiento;
    

    // Guarda el Enum como un String en la base de datos (más seguro que un número)
    @Enumerated(EnumType.STRING)
    private Especie especie;

    // --- Relaciones estructurales deducidas de tus métodos ---
    
    /*@OneToMany(mappedBy = "mascota")
    private List<HistorialMedico> historiales = new ArrayList<>();
    */

    @OneToMany(mappedBy = "mascota")
    private List<Turno> turnos = new ArrayList<>();
    

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    // 4. Métodos de Comportamiento (Modelo Rico)

    // Método que devolverá la lista de historiales médicos
    public List<?> obtenerHistorialMedico() {
        // return historiales; (Descomentar cuando exista la clase)
        return new ArrayList<>(); 
    } //Revisar si es necesario 

    // Valida si un nuevo turno se choca con los que ya tiene la mascota
    public boolean tieneTurnoSolapado(LocalDateTime fechaHoraNuevo, int duracionMinutosNuevo) {
        LocalDateTime finNuevo = fechaHoraNuevo.plusMinutes(duracionMinutosNuevo);

        for (Turno turnoExistente : turnos) {
            LocalDateTime inicioExistente = turnoExistente.getFechaHora();
            
            // ¡Magia de la POO! Le pedimos al turno existente que calcule su propio fin
            LocalDateTime finExistente = turnoExistente.calcularFechaHoraFin();

            if (fechaHoraNuevo.isBefore(finExistente) && finNuevo.isAfter(inicioExistente)) {
                return true; 
            }
        }
        
        return false; 
    }

    @Override
    public String toString() {
        return nombre + " (" + especie + " - " + raza + ")";
    }
}