package com.poo.servicio;

import com.poo.modelo.Veterinario;
import com.poo.repositorio.VeterinarioRepositorio;

import java.util.List;

public class VeterinarioServicio {

    private final VeterinarioRepositorio veterinarioRepositorio;


    public VeterinarioServicio() {
        this.veterinarioRepositorio = new VeterinarioRepositorio();
    }


    public void guardar(Veterinario veterinario) {

        if (veterinario.getNombre() == null || veterinario.getNombre().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del veterinario no puede estar vacío"
            );
        }

        veterinarioRepositorio.guardar(veterinario);
    }


    public List<Veterinario> listar() {
        return veterinarioRepositorio.listarTodos();
    }


    public Veterinario buscarPorId(Long id) {
        return veterinarioRepositorio.buscarPorId(id);
    }


    public void eliminar(Veterinario veterinario) {
        veterinarioRepositorio.eliminar(veterinario);
    }
}