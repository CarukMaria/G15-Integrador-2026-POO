package com.poo.servicio;

import com.poo.modelo.Servicio;
import com.poo.repositorio.ServicioRepositorio;

import java.util.List;

public class ServicioServicio {

    private final ServicioRepositorio servicioRepository;

    public ServicioServicio() {
        this.servicioRepository = new ServicioRepositorio();
    }

    public void guardar(Servicio servicio) {
        servicioRepository.guardar(servicio);
    }

    public List<Servicio> listar() {
        return servicioRepository.listarTodos();
    }

    public Servicio buscarPorId(Long id) {
        return servicioRepository.buscarPorId(id);
    }

    public void eliminar(Servicio servicio) {
        servicioRepository.eliminar(servicio);
    }
}
