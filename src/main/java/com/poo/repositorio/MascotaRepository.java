package com.poo.repositorio;

import com.poo.modelo.Mascota;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MascotaRepository {


    public void guardar(Mascota mascota) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(mascota);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    public Mascota buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Mascota.class, id);

        } finally {
            em.close();
        }
    }


    public List<Mascota> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<Mascota> query =
                    em.createQuery(
                            "SELECT m FROM Mascota m",
                            Mascota.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Mascota mascota) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Mascota eliminada = em.merge(mascota);
            em.remove(eliminada);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}