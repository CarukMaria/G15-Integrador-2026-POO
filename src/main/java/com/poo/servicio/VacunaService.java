package com.poo.servicio;

import com.poo.modelo.Vacuna;
import com.poo.repositorio.VacunaRepository;

import java.util.List;

public class VacunaService {

    private final VacunaRepository vacunaRepository;


    public VacunaService() {
        this.vacunaRepository = new VacunaRepository();
    }


    public void guardar(Vacuna vacuna) {

        if (vacuna.getNombre() == null || vacuna.getNombre().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la vacuna no puede estar vacío"
            );
        }

        vacunaRepository.guardar(vacuna);
    }


    public List<Vacuna> listar() {
        return vacunaRepository.listarTodos();
    }


    public Vacuna buscarPorId(Long id) {
        return vacunaRepository.buscarPorId(id);
    }


    public void eliminar(Vacuna vacuna) {
        vacunaRepository.eliminar(vacuna);
    }
}