package com.poo.servicio;

import com.poo.modelo.EstadoTurno;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.modelo.Vacunacion;
import com.poo.repositorio.TurnoRepositorio;

import java.time.LocalDate;
import java.util.List;

public class TurnoServicio {

    private final TurnoRepositorio turnoRepositorio;

    public TurnoServicio() {
        turnoRepositorio = new TurnoRepositorio();
    }

    /*
     * Guarda un turno aplicando las reglas que requieren
     * consultar otros turnos o información relacionada.
     */
    public void guardar(Turno turno) {

        validarDatosBasicos(turno);
        validarSolapamiento(turno);
        validarVacunas(turno);

        turnoRepositorio.guardar(turno);
    }

    /*
     * Valida los datos mínimos necesarios para guardar el turno.
     */
    private void validarDatosBasicos(Turno turno) {

        if (turno == null) {
            throw new IllegalArgumentException(
                "El turno no puede ser nulo"
            );
        }

        if (turno.getFechaHora() == null) {
            throw new IllegalArgumentException(
                "La fecha del turno no puede estar vacía"
            );
        }

        if (turno.getMascota() == null) {
            throw new IllegalArgumentException(
                "El turno debe tener una mascota"
            );
        }

        if (turno.getVeterinario() == null) {
            throw new IllegalArgumentException(
                "El turno debe tener un veterinario"
            );
        }
    }

    /*
     * Regla: una mascota y un veterinario no pueden tener
     * dos turnos que se superpongan.
     */
    private void validarSolapamiento(Turno nuevoTurno) {

        for (Turno existente : listar()) {

            // Si estamos modificando el turno, ignoramos el propio turno.
            if (nuevoTurno.getIdTurno() != null
                    && nuevoTurno.getIdTurno().equals(existente.getIdTurno())) {
                continue;
            }

            // Los turnos cancelados no ocupan el horario.
            if (existente.getEstado() == EstadoTurno.CANCELADO) {
                continue;
            }

            if (!nuevoTurno.seSuperponeCon(existente)) {
                continue;
            }

            if (existente.getMascota().getIdMascota()
                    .equals(nuevoTurno.getMascota().getIdMascota())) {

                throw new IllegalArgumentException(
                    "La mascota ya tiene un turno en ese horario."
                );
            }

            if (existente.getVeterinario().getIdVeterinario()
                    .equals(nuevoTurno.getVeterinario().getIdVeterinario())) {

                throw new IllegalArgumentException(
                    "El veterinario ya tiene un turno en ese horario."
                );
            }
        }
    }

    /*
     * Regla: una mascota no puede recibir nuevamente una vacuna
     * mientras la anterior siga vigente.
     */
    private void validarVacunas(Turno turno) {

        LocalDate fechaDelTurno = turno.getFechaHora().toLocalDate();

        for (ServicioPrestado servicio : turno.getServiciosPrestados()) {

            if (servicio.getServicio() instanceof Vacunacion vacunacion) {

                if (!turno.getMascota().puedeRecibirVacuna(
                        vacunacion.getVacuna(),
                        fechaDelTurno,
                        turno.getIdTurno())) {

                    throw new IllegalArgumentException(
                        "La mascota todavía tiene vigente esta vacuna para la fecha seleccionada."
                    );
                }
            }
        }
    }

    /*
     * Cambia el estado del turno. (Simplificado al delegar la fecha al turno)
     */
    public void cambiarEstado(
            Turno turno,
            EstadoTurno nuevoEstado) {

        turno.cambiarEstado(nuevoEstado);

        turnoRepositorio.guardar(turno);
    }

    public List<Turno> listar() {
        return turnoRepositorio.listarTodos();
    }

    public List<Turno> buscarPorFicha(String numeroFicha) {

        if (numeroFicha == null || numeroFicha.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "La ficha no puede estar vacía"
            );
        }

        return turnoRepositorio.buscarPorFicha(numeroFicha.trim());
    }

    public Turno buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException(
                "El id no puede ser nulo"
            );
        }

        return turnoRepositorio.buscarPorId(id);
    }

    public void eliminar(Turno turno) {
        turnoRepositorio.eliminar(turno);
    }
}