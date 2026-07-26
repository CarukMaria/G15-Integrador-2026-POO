package com.poo.controlador;

import com.poo.modelo.Turno;
import com.poo.servicio.TurnoService;

import java.util.List;

public class TurnoController {

    private final TurnoService turnoService;


    public TurnoController() {
        this.turnoService = new TurnoService();
    }


    public void guardar(Turno turno) {
        turnoService.guardar(turno);
    }


    public List<Turno> listar() {
        return turnoService.listar();
    }


    public Turno buscarPorId(Long id) {
        return turnoService.buscarPorId(id);
    }


    public void eliminar(Turno turno) {
        turnoService.eliminar(turno);
    }
}