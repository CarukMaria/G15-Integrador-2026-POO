package com.poo.controlador;

import com.poo.modelo.ServicioPrestado;
import com.poo.servicio.ServicioPrestadoService;

import java.util.List;

public class ServicioPrestadoControlador {

    private ServicioPrestadoService servicioPrestadoService;


    public ServicioPrestadoControlador() {
        servicioPrestadoService = new ServicioPrestadoService();
    }


    public void guardar(ServicioPrestado servicioPrestado) {
        servicioPrestadoService.guardar(servicioPrestado);
    }


    public List<ServicioPrestado> listar() {
        return servicioPrestadoService.listar();
    }


    public ServicioPrestado buscarPorId(Long id) {
        return servicioPrestadoService.buscarPorId(id);
    }


    public void eliminar(ServicioPrestado servicioPrestado) {
        servicioPrestadoService.eliminar(servicioPrestado);
    }
}