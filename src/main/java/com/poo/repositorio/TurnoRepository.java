package com.poo.repositorio;

import com.poo.modelo.Turno;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TurnoRepository {


    public void guardar(Turno turno) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            if (turno.getIdTurno() == null) {
                em.persist(turno);
            } else {
                turno = em.merge(turno);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }


    public Turno buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Turno.class, id);

        } finally {
            em.close();
        }
    }


    public List<Turno> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<Turno> query =
                    em.createQuery(
                            "SELECT t FROM Turno t",
                            Turno.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Turno turno) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Turno eliminado = em.merge(turno);
            em.remove(eliminado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}
