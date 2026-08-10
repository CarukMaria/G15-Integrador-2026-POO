package com.poo.modelo;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    // Constructor vacío por defecto que requiere JPA (protegido)
    protected ServicioPrestado() {
    }

    public ServicioPrestado(Servicio servicio, Turno turno) {
        
        // Usamos los setters para aplicar las validaciones de una sola vez
        setServicio(servicio);
        setTurno(turno);

        /*
         * Se guarda una copia del precio y duración
         * porque pueden cambiar en el catálogo en el futuro.
         */
        setPrecioServicioPrestado(servicio.getPrecio());
        setDuracionServicioPrestado(servicio.getDuracion());
    }

    // ----- GETTERS Y SETTERS PROTEGIDOS ----
    
    public Long getIdServicioPrestado() {
        return idServicioPrestado;
    }
    
    // No agregamos setIdServicioPrestado() porque lo autogenera la base de datos.

    public double getPrecioServicioPrestado() {
        return precioServicioPrestado;
    }

    public void setPrecioServicioPrestado(double precioServicioPrestado) {
        if (precioServicioPrestado < 0) {
            throw new IllegalArgumentException("El precio histórico del servicio prestado no puede ser negativo.");
        }
        this.precioServicioPrestado = precioServicioPrestado;
    }

    public int getDuracionServicioPrestado() {
        return duracionServicioPrestado;
    }

    public void setDuracionServicioPrestado(int duracionServicioPrestado) {
        if (duracionServicioPrestado <= 0) {
            throw new IllegalArgumentException("La duración histórica debe ser mayor a 0.");
        }
        this.duracionServicioPrestado = duracionServicioPrestado;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio asociado no puede ser nulo.");
        }
        this.servicio = servicio;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
}