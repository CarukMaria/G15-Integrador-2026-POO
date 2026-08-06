package com.poo.servicio;

import com.poo.modelo.ServicioPrestado;
import com.poo.repositorio.ServicioPrestadoRepositorio;

import java.util.List;

public class ServicioPrestadoServicio {

    private final ServicioPrestadoRepositorio servicioPrestadoRepositorio;


    public ServicioPrestadoServicio() {
        this.servicioPrestadoRepositorio = new ServicioPrestadoRepositorio();
    }


    public void guardar(ServicioPrestado servicioPrestado) {

        if (servicioPrestado.getFechaPrestacion() == null) {
            throw new IllegalArgumentException(
                    "La fecha de prestación no puede estar vacía"
            );
        }

        servicioPrestadoRepositorio.guardar(servicioPrestado);
    }


    public List<ServicioPrestado> listar() {
        return servicioPrestadoRepositorio.listarTodos();
    }


    public ServicioPrestado buscarPorId(Long id) {
        return servicioPrestadoRepositorio.buscarPorId(id);
    }


    public void eliminar(ServicioPrestado servicioPrestado) {
        servicioPrestadoRepositorio.eliminar(servicioPrestado);
    }
}