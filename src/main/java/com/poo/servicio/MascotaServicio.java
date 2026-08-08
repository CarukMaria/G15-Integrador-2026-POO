package com.poo.servicio;

import com.poo.modelo.Mascota;
import com.poo.repositorio.MascotaRepositorio;

import java.util.List;

public class MascotaServicio {

    private final MascotaRepositorio mascotaRepositorio;

    public MascotaServicio() {
        this.mascotaRepositorio = new MascotaRepositorio();
    }

    public void guardar(Mascota mascota) {
        mascotaRepositorio.guardar(mascota);
    }

    public List<Mascota> listar() {
        return mascotaRepositorio.listarTodos();
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepositorio.buscarPorId(id);
    }

    public void eliminar(Mascota mascota) {
        mascotaRepositorio.eliminar(mascota);
    }
}
