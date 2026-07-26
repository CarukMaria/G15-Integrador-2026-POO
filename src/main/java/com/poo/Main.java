package com.poo;

import com.poo.modelo.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando sistema...");

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("VeterinariaPU");

        EntityManager em = emf.createEntityManager();

        System.out.println("JPA inicializada correctamente.");

        Cliente cliente = new Cliente();
        cliente.setNombre("Carla");
        cliente.setApellido("Prueba");
        cliente.setDni("99999999");

        em.getTransaction().begin();

        em.persist(cliente);

        em.getTransaction().commit();

        System.out.println("Cliente guardado con ID: " + cliente.getIdCliente());

        em.close();
        emf.close();
    }
}