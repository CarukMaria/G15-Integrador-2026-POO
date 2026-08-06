package com.poo.controlador;

import com.poo.modelo.ServicioPrestado;
import com.poo.servicio.ServicioPrestadoServicio;

import java.util.List;

public class ServicioPrestadoControlador {

    private ServicioPrestadoServicio servicioPrestadoServicio;


    public ServicioPrestadoControlador() {
        servicioPrestadoServicio = new ServicioPrestadoServicio();
    }


    public void guardar(ServicioPrestado servicioPrestado) {
        servicioPrestadoServicio.guardar(servicioPrestado);
    }


    public List<ServicioPrestado> listar() {
        return servicioPrestadoServicio.listar();
    }


    public ServicioPrestado buscarPorId(Long id) {
        return servicioPrestadoServicio.buscarPorId(id);
    }


    public void eliminar(ServicioPrestado servicioPrestado) {
        servicioPrestadoServicio.eliminar(servicioPrestado);
    }
}