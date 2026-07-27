package com.poo.controlador;

import com.poo.modelo.Cliente;
import com.poo.servicio.ClienteService;

import java.util.List;

public class ClienteController {

    private ClienteService servicio;

    public ClienteController() {
        servicio = new ClienteService();
    }

    public void guardar(Cliente cliente) {
        servicio.guardar(cliente);
    }

    public List<Cliente> listar() {
        return servicio.listar();
    }

    public Cliente buscarPorId(Long id) {
        return servicio.buscarPorId(id);
    }

    public void eliminar(Cliente cliente) {
        servicio.eliminar(cliente);
    }
}