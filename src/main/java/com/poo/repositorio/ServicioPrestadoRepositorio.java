package com.poo.repositorio;

import com.poo.modelo.ServicioPrestado;
import com.poo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServicioPrestadoRepositorio {


    public void guardar(ServicioPrestado servicioPrestado) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(servicioPrestado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }


    public ServicioPrestado buscarPorId(Long id) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(ServicioPrestado.class, id);

        } finally {
            em.close();
        }
    }


    public List<ServicioPrestado> listarTodos() {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            TypedQuery<ServicioPrestado> query =
                    em.createQuery(
                            "SELECT sp FROM ServicioPrestado sp",
                            ServicioPrestado.class
                    );

            return query.getResultList();

        } finally {
            em.close();
        }
    }


    public void eliminar(ServicioPrestado servicioPrestado) {

        EntityManager em = JPAUtil.getEntityManager();

        try {

            em.getTransaction().begin();

            ServicioPrestado eliminado = em.merge(servicioPrestado);
            em.remove(eliminado);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }
}