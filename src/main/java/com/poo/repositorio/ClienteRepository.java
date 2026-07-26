package com.poo.repositorio;

import com.poo.modelo.Cliente;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClienteRepository {

    public void guardar(Cliente cliente) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(cliente);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    public Cliente buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Cliente.class, id);

        } finally {
            em.close();
        }
    }


    public List<Cliente> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<Cliente> query =
                    em.createQuery(
                        "SELECT c FROM Cliente c",
                        Cliente.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Cliente cliente) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Cliente eliminado = em.merge(cliente);
            em.remove(eliminado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}