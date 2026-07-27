package com.poo.servicio;

import com.poo.modelo.Cliente;
import com.poo.repositorio.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }

    public void guardar(Cliente cliente) {
        if (cliente.getDni() == null || cliente.getDni().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
        
        if (cliente.getTelefono() == null || cliente.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("El teléfono no puede estar vacío");
        }

        clienteRepository.guardar(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    public void eliminar(Cliente cliente) {
        clienteRepository.eliminar(cliente);
    }
}