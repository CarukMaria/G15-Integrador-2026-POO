package com.poo.servicio;

import com.poo.modelo.ServicioPrestado;
import com.poo.repositorio.ServicioPrestadoRepository;

import java.util.List;

public class ServicioPrestadoService {

    private final ServicioPrestadoRepository servicioPrestadoRepository;


    public ServicioPrestadoService() {
        this.servicioPrestadoRepository = new ServicioPrestadoRepository();
    }


    public void guardar(ServicioPrestado servicioPrestado) {

        if (servicioPrestado.getFechaPrestacion() == null) {
            throw new IllegalArgumentException(
                    "La fecha de prestación no puede estar vacía"
            );
        }

        servicioPrestadoRepository.guardar(servicioPrestado);
    }


    public List<ServicioPrestado> listar() {
        return servicioPrestadoRepository.listarTodos();
    }


    public ServicioPrestado buscarPorId(Long id) {
        return servicioPrestadoRepository.buscarPorId(id);
    }


    public void eliminar(ServicioPrestado servicioPrestado) {
        servicioPrestadoRepository.eliminar(servicioPrestado);
    }
}