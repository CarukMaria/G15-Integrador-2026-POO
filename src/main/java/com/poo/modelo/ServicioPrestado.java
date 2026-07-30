package com.poo.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "servicios_prestados")
public class ServicioPrestado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicioPrestado;

    @Column(nullable = false)
    private double precioServicioPrestado;

    @Column(nullable = false)
    private int duracionServicioPrestado;


    /*
     * Puede ser null mientras el turno está pendiente.
     * Se completa cuando el servicio realmente se realiza.
     */
    private LocalDateTime fechaPrestacion;


    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;


    public ServicioPrestado() {
    }

    public ServicioPrestado(
            Servicio servicio,
            Turno turno) {


        if (servicio == null) {

            throw new IllegalArgumentException(
                "El servicio no puede ser nulo."
            );
        }


        this.servicio = servicio;
        this.turno = turno;

        /*
         * Se guarda una copia del precio y duración
         * porque pueden cambiar en el futuro.
         */
        this.precioServicioPrestado =
                servicio.getPrecio();


        this.duracionServicioPrestado =
                servicio.getDuracion();


        this.fechaPrestacion = null;
    }


    public void registrarPrestacion() {


        if (fechaPrestacion != null) {

            throw new IllegalStateException(
                "El servicio ya fue realizado."
            );
        }

        fechaPrestacion = LocalDateTime.now();
    }

    // ----- GETTERS Y SETTERS ----
    public Long getIdServicioPrestado() {
        return idServicioPrestado;
    }

    public void setIdServicioPrestado(
            Long idServicioPrestado) {

        this.idServicioPrestado = idServicioPrestado;
    }

    public double getPrecioServicioPrestado() {

        return precioServicioPrestado;
    }

    public void setPrecioServicioPrestado(
            double precioServicioPrestado) {

        this.precioServicioPrestado = precioServicioPrestado;
    }

    public int getDuracionServicioPrestado() {

        return duracionServicioPrestado;
    }

    public void setDuracionServicioPrestado(
            int duracionServicioPrestado) {

        this.duracionServicioPrestado =
                duracionServicioPrestado;
    }

    public LocalDateTime getFechaPrestacion() {

        return fechaPrestacion;
    }

    public void setFechaPrestacion(
            LocalDateTime fechaPrestacion) {

        this.fechaPrestacion = fechaPrestacion;
    }

    public Servicio getServicio() {

        return servicio;
    }

    public void setServicio(Servicio servicio) {

        this.servicio = servicio;
    }


    public Turno getTurno() {

        return turno;
    }

    public void setTurno(Turno turno) {

        this.turno = turno;
    }
}