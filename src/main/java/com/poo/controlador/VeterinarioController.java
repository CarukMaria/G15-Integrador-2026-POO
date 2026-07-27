package com.poo.controlador;

import com.poo.modelo.Veterinario;
import com.poo.servicio.VeterinarioService;

import java.util.List;

public class VeterinarioController {

    private VeterinarioService servicio;


    public VeterinarioController() {
        servicio = new VeterinarioService();
    }


    public void guardar(Veterinario veterinario) {
        servicio.guardar(veterinario);
    }


    public List<Veterinario> listar() {
        return servicio.listar();
    }


    public Veterinario buscarPorId(Long id) {
        return servicio.buscarPorId(id);
    }


    public void eliminar(Veterinario veterinario) {
        servicio.eliminar(veterinario);
    }
}