package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;



import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Category;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.sql.SQLException;
import java.util.List;

public class CategoryRepository extends DAO<Category> {

    public CategoryRepository() {
    }

    @Override
    public void create(Category obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.persist(obj);
    }

    @Override
    public List<Category> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Category> query = em.createQuery("SELECT a FROM Category a", Category.class);
        return query.getResultList();
    }

    @Override
    public Category findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Category.class, id);
    }

    @Override
    public void update(Category obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.merge(obj);
    }

    @Override
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Category c = em.find(Category.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}
