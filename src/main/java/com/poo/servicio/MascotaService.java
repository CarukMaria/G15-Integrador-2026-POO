package com.poo.servicio;

import com.poo.modelo.Mascota;
import com.poo.repositorio.MascotaRepository;

import java.util.List;

public class MascotaService {

    private final MascotaRepository mascotaRepository;


    public MascotaService() {
        this.mascotaRepository = new MascotaRepository();
    }


    public void guardar(Mascota mascota) {

        if (mascota.getNombre() == null || mascota.getNombre().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la mascota no puede estar vacío"
            );
        }

        mascotaRepository.guardar(mascota);
    }


    public List<Mascota> listar() {
        return mascotaRepository.listarTodos();
    }


    public Mascota buscarPorId(Long id) {
        return mascotaRepository.buscarPorId(id);
    }


    public void eliminar(Mascota mascota) {
        mascotaRepository.eliminar(mascota);
    }
}