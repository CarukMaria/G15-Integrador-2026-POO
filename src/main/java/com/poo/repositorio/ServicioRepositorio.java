package com.poo.repositorio;

import com.poo.modelo.Servicio;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServicioRepositorio {


    public void guardar(Servicio servicio) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            if (servicio.getIdServicio() == null) {
                em.persist(servicio);
            } else {
                em.merge(servicio);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            // Si algo sale mal, deshacemos los cambios en la BD por seguridad
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public Servicio buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Servicio.class, id);

        } finally {
            em.close();
        }
    }


    public List<Servicio> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<Servicio> query =
                    em.createQuery(
                            "SELECT s FROM Servicio s",
                            Servicio.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Servicio servicio) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Servicio eliminado = em.merge(servicio);
            em.remove(eliminado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}