package com.poo.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;


    @Column(nullable = false)
    private LocalDateTime fechaHora;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTurno estado;


    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    @OneToMany(
        mappedBy = "turno",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private List<ServicioPrestado> serviciosPrestados =
            new ArrayList<>();


    public Turno() {
    }

    public Turno(
        LocalDateTime fechaHora,
        Mascota mascota,
        Veterinario veterinario) {

        this.fechaHora = fechaHora;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.estado = EstadoTurno.PENDIENTE;
    }


    /*
     * REGLAS DE NEGOCIO DEL ESTADO
     */

    public void cambiarEstado(EstadoTurno nuevoEstado) {

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        // Si el estado es el mismo que ya tiene, no hacemos nada
        if (this.estado == nuevoEstado) {
            return;
        }

        // --- REGLAS ESTRICTAS DE ESTADOS TERMINALES ---
        if (this.estado == EstadoTurno.ATENDIDO) {
            throw new IllegalStateException(
                "Un turno ATENDIDO no puede volver a ningún otro estado."
            );
        }

        if (this.estado == EstadoTurno.CANCELADO) {
            throw new IllegalStateException(
                "Un turno CANCELADO no puede reactivarse."
            );
        }
        // ----------------------------------------------

        switch (nuevoEstado) {
            case CONFIRMADO -> confirmar();
            case ATENDIDO -> atender();
            case CANCELADO -> cancelar();
            case PENDIENTE -> {
                throw new IllegalStateException(
                    "No se puede volver al estado PENDIENTE de forma manual."
                );
            }
        }
    }


    private void confirmar() {

        // (Esta validación extra ya está cubierta por el bloque superior, 
        // pero la dejamos por doble seguridad semántica)
        if (estado == EstadoTurno.CANCELADO) {
            throw new IllegalStateException(
                "Un turno cancelado no puede confirmarse"
            );
        }

        estado = EstadoTurno.CONFIRMADO;
    }


    private void atender() {

        // REGLA: No puede pasar de PENDIENTE a ATENDIDO sin ser CONFIRMADO
        if (estado != EstadoTurno.CONFIRMADO) {
            throw new IllegalStateException(
                "Debe confirmarse antes de atenderse"
            );
        }

        estado = EstadoTurno.ATENDIDO;
    }


    private void cancelar() {

        // REGLA: Control de las 24 horas
        if (LocalDateTime.now().isAfter(fechaHora.minusHours(24))) {
            throw new IllegalStateException(
                "No se puede cancelar con menos de 24 horas de anticipación."
            );
        }

        estado = EstadoTurno.CANCELADO;
    }




    /*
     * LÓGICA PROPIA DEL TURNO
     */


    public int calcularDuracionTotal() {

        int total = 0;


        for (ServicioPrestado servicio :
                serviciosPrestados) {

            total += servicio.getDuracionServicioPrestado();
        }


        return total;
    }



    public double calcularPrecioFinal() {

        double total = 0;


        for (ServicioPrestado servicio :
                serviciosPrestados) {

            total += servicio.getPrecioServicioPrestado();
        }


        return total;
    }



    public LocalDateTime calcularFechaHoraFin() {

        return fechaHora.plusMinutes(
                calcularDuracionTotal()
        );
    }



    public void agregarServicioPrestado(
            ServicioPrestado servicio) {


        if (servicio == null) {

            throw new IllegalArgumentException(
                "El servicio no puede ser nulo"
            );
        }


        serviciosPrestados.add(servicio);

        servicio.setTurno(this);
    }


    public boolean seSuperponeCon(Turno otro) {

        LocalDateTime inicio1 = this.fechaHora;
        LocalDateTime fin1 = this.calcularFechaHoraFin();

        LocalDateTime inicio2 = otro.fechaHora;
        LocalDateTime fin2 = otro.calcularFechaHoraFin();

        return inicio1.isBefore(fin2)
                &&
               inicio2.isBefore(fin1);
    }

    public boolean tieneVacuna(String nombreVacuna) {


        for (ServicioPrestado servicio :
                serviciosPrestados) {


            if (servicio.getServicio()
                    instanceof Vacunacion vacuna) {


                if (vacuna.getNombreVacuna()
                        .equalsIgnoreCase(nombreVacuna)) {

                    return true;
                }
            }
        }


        return false;
    }


// --------- GETTERS Y SETTERS --------

    public Long getIdTurno() {
        return idTurno;
    }


    public LocalDateTime getFechaHora() {
        return fechaHora;
    }


    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }


    public EstadoTurno getEstado() {
        return estado;
    }


    public Mascota getMascota() {
        return mascota;
    }


    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }


    public Veterinario getVeterinario() {
        return veterinario;
    }


    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }


    public List<ServicioPrestado> getServiciosPrestados() {
        return serviciosPrestados;
    }


    public void setServiciosPrestados(
            List<ServicioPrestado> serviciosPrestados) {

        this.serviciosPrestados = serviciosPrestados;
    }
}