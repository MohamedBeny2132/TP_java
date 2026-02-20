package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;

import java.util.List;

public class UserRepository extends DAO<User> {

    public UserRepository() {
    }

    @Override
    public void create(User obj, EntityManager em) {
        em.persist(obj);
    }

    @Override
    public List<User> findAll(EntityManager em) {
        TypedQuery<User> query = em.createQuery("SELECT a FROM User a", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(int id, EntityManager em) {
        return em.find(User.class, id);
    }

    @Override
    public void update(User obj, EntityManager em) {
        em.merge(obj);
    }

    @Override
    public void delete(int id, EntityManager em) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }

    public User findByEmail(String email, EntityManager em) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
