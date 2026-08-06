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

        if (servicio.getNombre() == null || servicio.getNombre().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del servicio no puede estar vacío"
            );
        }

        if (servicio.getPrecio() <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor a 0"
            );
        }

        if (servicio.getDuracion() <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor a 0"
            );
        }

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