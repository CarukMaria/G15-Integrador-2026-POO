package com.poo.controlador;

import com.poo.modelo.Vacuna;
import com.poo.servicio.VacunaService;

import java.util.List;

public class VacunaController {

    private VacunaService servicio;


    public VacunaController() {
        servicio = new VacunaService();
    }


    public void guardar(Vacuna vacuna) {
        servicio.guardar(vacuna);
    }


    public List<Vacuna> listar() {
        return servicio.listar();
    }


    public Vacuna buscarPorId(Long id) {
        return servicio.buscarPorId(id);
    }


    public void eliminar(Vacuna vacuna) {
        servicio.eliminar(vacuna);
    }
}