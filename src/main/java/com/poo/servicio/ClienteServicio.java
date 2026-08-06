package com.poo.servicio;

import com.poo.modelo.Cliente;
import com.poo.repositorio.ClienteRepositorio;

import java.util.List;

public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;

    public ClienteServicio() {
        this.clienteRepositorio = new ClienteRepositorio();
    }

    public void guardar(Cliente cliente) {
        if (cliente.getDni() == null || cliente.getDni().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
        
        if (cliente.getTelefono() == null || cliente.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        clienteRepositorio.guardar(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepositorio.listarTodos();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepositorio.buscarPorId(id);
    }

    public void eliminar(Cliente cliente) {
        clienteRepositorio.eliminar(cliente);
    }
}