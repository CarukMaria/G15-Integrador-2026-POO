package com.poo.controlador;

import com.poo.modelo.Servicio;
import com.poo.servicio.ServicioService;

import java.util.List;

public class ServicioController {

    private ServicioService servicio;


    public ServicioController() {
        servicio = new ServicioService();
    }


    public void guardar(Servicio servicio) {
        this.servicio.guardar(servicio);
    }


    public List<Servicio> listar() {
        return servicio.listar();
    }


    public Servicio buscarPorId(Long id) {
        return servicio.buscarPorId(id);
    }


    public void eliminar(Servicio servicio) {
        this.servicio.eliminar(servicio);
    }
}