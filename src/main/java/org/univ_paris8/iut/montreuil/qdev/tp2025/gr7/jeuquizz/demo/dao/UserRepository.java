package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.util.List;



public class UserRepository extends DAO<User> {

    public UserRepository() {
    }

    @Override
    public void create(User obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.persist(obj);
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT a FROM User a", User.class);
        return query.getResultList();
    }

    @Override
    public User findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(User.class, id);
    }

    @Override
    public void update(User obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.merge(obj);
    }

    @Override
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }
}
