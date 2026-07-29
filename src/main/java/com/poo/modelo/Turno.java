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

    // Guardamos el Enum como texto en la base de datos
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTurno estado;

    /* --- RELACIONES CON OTRAS CLASES --- */

    // Muchos turnos pueden ser para una misma mascota
    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    // Muchos turnos pueden ser atendidos por el mismo veterinario
    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Veterinario veterinario;

    // Un turno puede tener varios servicios (ej: Vacuna + Baño) 
    // y un servicio puede estar en muchos turnos. Se crea una tabla intermedia.
    @OneToMany(
        mappedBy = "turno",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER // <- ¡Esta es la línea mágica que faltaba!
    )
    private List<ServicioPrestado> serviciosPrestados = new ArrayList<>();

    // 1. Constructor vacío requerido por JPA
    public Turno() {
    }

    // 2. Constructor con parámetros
    public Turno(LocalDateTime fechaHora, Mascota mascota, Veterinario veterinario) {
        this.fechaHora = fechaHora;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.estado = EstadoTurno.PENDIENTE; // Todo turno nace como pendiente por defecto
    }

    /* --- MÉTODOS DE NEGOCIO --- */

    public void confirmar() {
        this.estado = EstadoTurno.CONFIRMADO;
    }

    public void atender() {
        this.estado = EstadoTurno.ATENDIDO;
    }

    public void cancelar() {
        this.estado = EstadoTurno.CANCELADO;
    }

    // Calcula el total sumando el precio de cada servicio de la lista
    public double calcularPrecioFinal() {
        double total = 0.0;
        for (ServicioPrestado servicio : serviciosPrestados) {
            // Asumo que tu clase Servicio tiene un método getPrecio()
            total += servicio.getPrecioServicioPrestado(); 
        }
        return total;
    }

    // Calcula la duración sumando el tiempo de cada servicio
    public int calcularDuracionTotal() {
        int totalMinutos = 0;
        for (ServicioPrestado servicio : serviciosPrestados) {
            // Asumo que tu clase Servicio tiene un atributo de duración y su getDuracion()
            totalMinutos += servicio.getDuracionServicioPrestado(); 
        }
        return totalMinutos;
    }

    // Calcula la fecha y hora exacta de finalización sumando los minutos de los servicios
    public LocalDateTime calcularFechaHoraFin() {
        return this.fechaHora.plusMinutes(calcularDuracionTotal());
    }

    // Método extra útil para agregar servicios al turno
    public void agregarServicioPrestado(ServicioPrestado servicio) {
        serviciosPrestados.add(servicio);
        servicio.setTurno(this);
    }

    /* --- GETTERS Y SETTERS --- */

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

    public void setEstado(EstadoTurno nuevoEstado) {
        if (this.estado == EstadoTurno.ATENDIDO && nuevoEstado == EstadoTurno.CANCELADO) {
            throw new IllegalStateException("Regla de negocio violada: No se puede cancelar un turno ya atendido.");
        }
        this.estado = nuevoEstado;
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

    public void setServiciosPrestados(List<ServicioPrestado> serviciosPrestados) {
        this.serviciosPrestados = serviciosPrestados;
    }
}