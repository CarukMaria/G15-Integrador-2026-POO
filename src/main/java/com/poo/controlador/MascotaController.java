package com.poo.controlador;

import com.poo.modelo.Mascota;
import com.poo.servicio.MascotaService;

import java.util.List;

public class MascotaController {

    private MascotaService servicio;


    public MascotaController() {
        servicio = new MascotaService();
    }


    public void guardar(Mascota mascota) {
        servicio.guardar(mascota);
    }


    public List<Mascota> listar() {
        return servicio.listar();
    }


    public Mascota buscarPorId(Long id) {
        return servicio.buscarPorId(id);
    }


    public void eliminar(Mascota mascota) {
        servicio.eliminar(mascota);
    }
}