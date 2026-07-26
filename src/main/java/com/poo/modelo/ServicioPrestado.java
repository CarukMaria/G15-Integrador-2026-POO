package com.poo.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "servicios_prestados")
public class ServicioPrestado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicioPrestado;

    private double precioServicioPrestado;
    private int duracionServicioPrestado;

    // Relación con Servicio (Muchos servicios prestados corresponden a un Servicio)
    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    // Relación con Turno (Muchos servicios prestados pertenecen a un Turno)
    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    // Constructor vacío obligatorio por JPA
    public ServicioPrestado() {
    }

    // Constructor con parámetros
    public ServicioPrestado(double precioServicioPrestado, int duracionServicioPrestado, Servicio servicio, Turno turno) {
        this.precioServicioPrestado = precioServicioPrestado;
        this.duracionServicioPrestado = duracionServicioPrestado;
        this.servicio = servicio;
        this.turno = turno;
    }

    // Getters y Setters
    public Long getIdServicioPrestado() {
        return idServicioPrestado;
    }

    public void setIdServicioPrestado(Long idServicioPrestado) {
        this.idServicioPrestado = idServicioPrestado;
    }

    public double getPrecioServicioPrestado() {
        return precioServicioPrestado;
    }

    public void setPrecioServicioPrestado(double precioServicioPrestado) {
        this.precioServicioPrestado = precioServicioPrestado;
    }

    public int getDuracionServicioPrestado() {
        return duracionServicioPrestado;
    }

    public void setDuracionServicioPrestado(int duracionServicioPrestado) {
        this.duracionServicioPrestado = duracionServicioPrestado;
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