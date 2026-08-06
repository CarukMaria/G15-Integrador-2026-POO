package com.poo.repositorio;

import com.poo.modelo.Veterinario;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VeterinarioRepositorio {


    public void guardar(Veterinario veterinario) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(veterinario);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    public Veterinario buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Veterinario.class, id);

        } finally {
            em.close();
        }
    }


    public List<Veterinario> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<Veterinario> query =
                    em.createQuery(
                            "SELECT v FROM Veterinario v",
                            Veterinario.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Veterinario veterinario) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Veterinario eliminado = em.merge(veterinario);
            em.remove(eliminado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}