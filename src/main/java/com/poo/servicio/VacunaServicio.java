package com.poo.servicio;

import com.poo.modelo.Vacuna;
import com.poo.repositorio.VacunaRepositorio;

import java.util.List;

public class VacunaServicio {

    private final VacunaRepositorio vacunaRepositorio;

    public VacunaServicio() {
        this.vacunaRepositorio = new VacunaRepositorio();
    }

    public void guardar(Vacuna vacuna) {
        vacunaRepositorio.guardar(vacuna);
    }

    public List<Vacuna> listar() {
        return vacunaRepositorio.listarTodos();
    }

    public Vacuna buscarPorId(Long id) {
        return vacunaRepositorio.buscarPorId(id);
    }

    public void eliminar(Vacuna vacuna) {
        vacunaRepositorio.eliminar(vacuna);
    }
}
