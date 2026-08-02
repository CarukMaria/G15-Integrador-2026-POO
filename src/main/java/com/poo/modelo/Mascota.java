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
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
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


    // Constructor vacío obligatorio por JPA (protegido)
    protected Mascota() {
    }


    // Constructor con parámetros que pasa por las validaciones
    public Mascota(String numeroFicha, String nombre, String raza,
                   LocalDate fechaNacimiento, Especie especie) {

        setNumeroFicha(numeroFicha);
        setNombre(nombre);
        setRaza(raza);
        setFechaNacimiento(fechaNacimiento);
        setEspecie(especie);
    }

    // ------- MÉTODOS DE NEGOCIO ---------

    // Comportamiento inteligente: la mascota calcula su propia edad
    public int calcularEdadEnAnios() {
        if (this.fechaNacimiento == null) {
            return 0;
        }
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    // Método que devolverá la lista de historiales médicos
    public List<?> obtenerHistorialMedico() {
        return new ArrayList<>();
    }
z
    // Valida si un nuevo turno se choca con los que ya tiene la mascota
    public boolean tieneTurnoSolapado(LocalDateTime fechaHoraNuevo,
                                      int duracionMinutosNuevo) {

        LocalDateTime finNuevo =
                fechaHoraNuevo.plusMinutes(duracionMinutosNuevo);


        for (Turno turnoExistente : turnos) {
            LocalDateTime inicioExistente = turnoExistente.getFechaHora();
            LocalDateTime finExistente = turnoExistente.calcularFechaHoraFin();

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
            
            if (idTurnoActual != null && idTurnoActual.equals(turno.getIdTurno())) {
                continue;
            }

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


    // --- VALIDACIONES INTERNAS PRIVADAS ---

    private String validarCadenaNoVacia(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " de la mascota no puede estar vacío.");
        }
        return valor.trim();
    }


    // --- GETTERS Y SETTERS PROTEGIDOS ---

    public Long getIdMascota() {
        return idMascota;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = validarCadenaNoVacia(numeroFicha, "Número de Ficha");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCadenaNoVacia(nombre, "Nombre");
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        // La raza podría ser opcional ("Mestizo"), pero si la pasan, que no sean espacios.
        this.raza = raza != null ? raza.trim() : "Mestizo";
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede estar en el futuro.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        if (especie == null) {
            throw new IllegalArgumentException("Debe clasificar la especie de la mascota.");
        }
        this.especie = especie;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Turno> getTurnos() {
        // Devolvemos una lista inmodificable para proteger el estado interno de la mascota
        return Collections.unmodifiableList(turnos);
    }
}