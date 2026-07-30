package com.poo.servicio;

import com.poo.modelo.EstadoTurno;
import com.poo.modelo.ServicioPrestado;
import com.poo.modelo.Turno;
import com.poo.modelo.Vacunacion;
import com.poo.repositorio.TurnoRepository;

import java.util.List;

public class TurnoService {


    private final TurnoRepository turnoRepository;

    public TurnoService() {

    turnoRepository = new TurnoRepository();
    }


    /*
     * Guarda un turno aplicando reglas que necesitan consultar BD
     */
    public void guardar(Turno turno) {

        validarDatosBasicos(turno);
        validarSolapamiento(turno);
        validarVacunas(turno);

        turnoRepository.guardar(turno);
    }


    private void validarDatosBasicos(Turno turno) {


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
     * Regla: un veterinario y una mascota no pueden tener
     * dos turnos que se pisen.
     */
    private void validarSolapamiento(Turno nuevoTurno) {

        for (Turno existente : listar()) {

            if (nuevoTurno.getIdTurno() != null &&
                nuevoTurno.getIdTurno().equals(existente.getIdTurno())) {
                continue;
            }


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
     * Regla: una mascota no puede recibir la misma vacuna
     * si ya la recibió dentro del último mes.
     */
private void validarVacunas(Turno turno) {

    for (ServicioPrestado servicio :
            turno.getServiciosPrestados()) {


        if (servicio.getServicio() instanceof Vacunacion vacunacion) {


            if (!turno.getMascota()
                    .puedeRecibirVacuna(
                        vacunacion.getVacuna()
                    )) {


                throw new IllegalArgumentException(
                    "La mascota todavía tiene vigente esta vacuna."
                );
            }
        }
    }
}


    public void cambiarEstado(
        Turno turno,
        EstadoTurno nuevoEstado) {


        turno.cambiarEstado(nuevoEstado);


        if (nuevoEstado == EstadoTurno.ATENDIDO) {

            for (ServicioPrestado servicio :
                    turno.getServiciosPrestados()) {

                if (servicio.getFechaPrestacion() == null) {
                    servicio.registrarPrestacion();
                }
            }
        }


        turnoRepository.guardar(turno);
    }


    public List<Turno> listar() {

        return turnoRepository.listarTodos();
    }


    public Turno buscarPorId(Long id) {


        if (id == null) {

            throw new IllegalArgumentException(
                "El id no puede ser nulo"
            );
        }


        return turnoRepository.buscarPorId(id);
    }


    public void eliminar(Turno turno) {

        turnoRepository.eliminar(turno);
    }

}