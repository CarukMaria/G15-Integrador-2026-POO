package com.poo.servicio;

import com.poo.modelo.Turno;
import com.poo.repositorio.TurnoRepository;

import java.util.List;

public class TurnoService {

    private final TurnoRepository turnoRepository;


    public TurnoService() {
        this.turnoRepository = new TurnoRepository();
    }


    public void guardar(Turno turno) {

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