package com.poo.servicio;

import com.poo.modelo.Veterinario;
import com.poo.repositorio.VeterinarioRepository;

import java.util.List;

public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;


    public VeterinarioService() {
        this.veterinarioRepository = new VeterinarioRepository();
    }


    public void guardar(Veterinario veterinario) {

        if (veterinario.getNombre() == null || veterinario.getNombre().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del veterinario no puede estar vacío"
            );
        }

        veterinarioRepository.guardar(veterinario);
    }


    public List<Veterinario> listar() {
        return veterinarioRepository.listarTodos();
    }


    public Veterinario buscarPorId(Long id) {
        return veterinarioRepository.buscarPorId(id);
    }


    public void eliminar(Veterinario veterinario) {
        veterinarioRepository.eliminar(veterinario);
    }
}