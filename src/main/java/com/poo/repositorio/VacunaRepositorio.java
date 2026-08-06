package com.poo.repositorio;

import com.poo.modelo.Vacuna;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VacunaRepositorio {


    public void guardar(Vacuna vacuna) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(vacuna);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    public Vacuna buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Vacuna.class, id);

        } finally {
            em.close();
        }
    }


    public List<Vacuna> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<Vacuna> query =
                    em.createQuery(
                            "SELECT v FROM Vacuna v",
                            Vacuna.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(Vacuna vacuna) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            Vacuna eliminada = em.merge(vacuna);
            em.remove(eliminada);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}