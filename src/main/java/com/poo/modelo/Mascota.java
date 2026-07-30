package com.poo.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String numeroFicha;
    private String nombre;
    private String raza;
    private LocalDate fechaNacimiento;


    @Enumerated(EnumType.STRING)
    private Especie especie;


    @OneToMany(mappedBy = "mascota", fetch = FetchType.EAGER)
    private List<Turno> turnos = new ArrayList<>();


    // Constructor vacío obligatorio por JPA
    public Mascota() {
    }


    // Constructor con parámetros
    public Mascota(String numeroFicha, String nombre, String raza,
                   LocalDate fechaNacimiento, Especie especie) {

        this.numeroFicha = numeroFicha;
        this.nombre = nombre;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.especie = especie;
    }


    // Getters y Setters

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



    // ---------------- MÉTODOS DE NEGOCIO ----------------


    // Método que devolverá la lista de historiales médicos
    public List<?> obtenerHistorialMedico() {
        return new ArrayList<>();
    }



    // Valida si un nuevo turno se choca con los que ya tiene la mascota
    public boolean tieneTurnoSolapado(LocalDateTime fechaHoraNuevo,
                                      int duracionMinutosNuevo) {

        LocalDateTime finNuevo =
                fechaHoraNuevo.plusMinutes(duracionMinutosNuevo);


        for (Turno turnoExistente : turnos) {

            LocalDateTime inicioExistente =
                    turnoExistente.getFechaHora();


            LocalDateTime finExistente =
                    turnoExistente.calcularFechaHoraFin();


            if (fechaHoraNuevo.isBefore(finExistente)
                    && finNuevo.isAfter(inicioExistente)) {

                return true;
            }
        }

        return false;
    }



// 1. Método original que usa el Controlador (antes de crear el turno, sin ID)
    public boolean puedeRecibirVacuna(Vacuna vacuna, LocalDate fechaNuevoTurno) {
        return puedeRecibirVacuna(vacuna, fechaNuevoTurno, null);
    }

    // 2. Nuevo método sobrecargado que usa el Service (ignora el turno actual)
    public boolean puedeRecibirVacuna(Vacuna vacuna, LocalDate fechaNuevoTurno, Long idTurnoActual) {

        for (Turno turno : turnos) {
            
            // EL FIX: Ignorar el turno actual para que no se bloquee a sí mismo
            if (idTurnoActual != null && idTurnoActual.equals(turno.getIdTurno())) {
                continue;
            }

            // Solo se consideran vacunas de turnos realizados
            if (turno.getEstado() != EstadoTurno.ATENDIDO) {
                continue;
            }

            for (ServicioPrestado servicioPrestado : turno.getServiciosPrestados()) {
                
                if (servicioPrestado.getServicio() instanceof Vacunacion) {
                    
                    Vacunacion vacunacion = (Vacunacion) servicioPrestado.getServicio();
                    
                    if (vacunacion.getNombreVacuna().equals(vacuna.getNombre())) {
                        
                        if (vacuna.estaVigente(
                                servicioPrestado.getFechaPrestacion().toLocalDate(), 
                                fechaNuevoTurno)) {
                            
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }



    @Override
    public String toString() {
        return nombre + " (" + especie + " - " + raza + ")";
    }
}