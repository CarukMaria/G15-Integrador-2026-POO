package com.poo.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
     * Estos atributos corresponden a la prestación concreta
     * de una consulta realizada dentro de un turno.
     *
     * No todas las prestaciones son consultas, por eso pueden
     * permanecer nulos mientras el servicio no sea una Consulta.
     */
    private String diagnostico;

    private String tratamiento;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turno;

    // Constructor vacío requerido por JPA
    protected ServicioPrestado() {
    }

    public ServicioPrestado(Servicio servicio, Turno turno) {

        setServicio(servicio);
        setTurno(turno);

        /*
         * Se guarda una copia del precio y duración
         * porque pueden cambiar en el catálogo en el futuro.
         */
        setPrecioServicioPrestado(servicio.getPrecio());
        setDuracionServicioPrestado(servicio.getDuracion());
    }

    // ----- MÉTODOS DE NEGOCIO -----

    /*
     * Registra el resultado de una consulta realizada.
     *
     * Solo tiene sentido cuando el servicio prestado corresponde
     * a una Consulta.
     */
    public void registrarConsulta(
            String diagnostico,
            String tratamiento) {

        if (!(servicio instanceof Consulta)) {
            throw new IllegalStateException(
                "Solo se puede registrar diagnóstico y tratamiento "
                + "para una consulta."
            );
        }

        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Para registrar la consulta, el diagnóstico es obligatorio."
            );
        }

        if (tratamiento == null || tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Para registrar la consulta, debe indicar un tratamiento o recomendaciones."
            );
        }

        this.diagnostico = diagnostico.trim();
        this.tratamiento = tratamiento.trim();
    }

    /*
     * Indica si esta prestación corresponde a una consulta
     * que ya tiene registrados sus datos médicos.
     */
    public boolean estaCompletada() {

        if (!(servicio instanceof Consulta)) {
            return true;
        }

        return diagnostico != null
                && !diagnostico.trim().isEmpty()
                && tratamiento != null
                && !tratamiento.trim().isEmpty();
    }

    // ----- GETTERS Y SETTERS -----

    public Long getIdServicioPrestado() {
        return idServicioPrestado;
    }

    public double getPrecioServicioPrestado() {
        return precioServicioPrestado;
    }

    public void setPrecioServicioPrestado(double precioServicioPrestado) {

        if (precioServicioPrestado < 0) {
            throw new IllegalArgumentException(
                "El precio histórico del servicio prestado no puede ser negativo."
            );
        }

        this.precioServicioPrestado = precioServicioPrestado;
    }

    public int getDuracionServicioPrestado() {
        return duracionServicioPrestado;
    }

    public void setDuracionServicioPrestado(int duracionServicioPrestado) {

        if (duracionServicioPrestado <= 0) {
            throw new IllegalArgumentException(
                "La duración histórica debe ser mayor a 0."
            );
        }

        this.duracionServicioPrestado = duracionServicioPrestado;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {

        if (diagnostico != null && diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Si se ingresa un diagnóstico, no puede ser un texto en blanco."
            );
        }

        this.diagnostico =
            diagnostico != null ? diagnostico.trim() : null;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {

        if (tratamiento != null && tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Si se ingresa un tratamiento, no puede ser un texto en blanco."
            );
        }

        this.tratamiento =
            tratamiento != null ? tratamiento.trim() : null;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                "El servicio asociado no puede ser nulo."
            );
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