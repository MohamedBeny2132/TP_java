package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.sql.SQLException;
import java.util.List;

public class AnnonceRepository extends DAO<Annonce> {

    public AnnonceRepository() {
    }

    @Override
    public void create(Annonce obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.persist(obj);
    }

    @Override
    public List<Annonce> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Annonce> query = em.createQuery("SELECT a FROM Annonce a", Annonce.class);
        return query.getResultList();
    }

    public PaginatedResponses<Annonce> findAllWithParam(AnnonceSearchParams params) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            // Requête principale pour récupérer les annonces filtrées
            StringBuilder jpql = new StringBuilder("SELECT a FROM Annonce a WHERE 1=1");

            if (params.getKeyword() != null && !params.getKeyword().isBlank()) {
                jpql.append(" AND (LOWER(a.title) LIKE :keyword OR LOWER(a.description) LIKE :keyword)");
            }
            if (params.getCategoryId() != null) {
                jpql.append(" AND a.category.id = :categoryId");
            }
            if (params.getStatus() != null) {
                jpql.append(" AND a.status = :status");
            }

            jpql.append(" ORDER BY a.date DESC");

            TypedQuery<Annonce> query = em.createQuery(jpql.toString(), Annonce.class);

            if (params.getKeyword() != null && !params.getKeyword().isBlank()) {
                query.setParameter("keyword", "%" + params.getKeyword().toLowerCase() + "%");
            }
            if (params.getCategoryId() != null) {
                query.setParameter("categoryId", params.getCategoryId());
            }
            if (params.getStatus() != null) {
                query.setParameter("status", params.getStatus());
            }

            // Pagination
            int page = params.getPage();
            int size = params.getSize();
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
            List<Annonce> annonces = query.getResultList();

            // Requête COUNT pour total
            StringBuilder countJpql = new StringBuilder("SELECT COUNT(a) FROM Annonce a WHERE 1=1");
            if (params.getKeyword() != null && !params.getKeyword().isBlank()) {
                countJpql.append(" AND (LOWER(a.title) LIKE :keyword OR LOWER(a.description) LIKE :keyword)");
            }
            if (params.getCategoryId() != null) {
                countJpql.append(" AND a.category.id = :categoryId");
            }
            if (params.getStatus() != null) {
                countJpql.append(" AND a.status = :status");
            }

            TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);
            if (params.getKeyword() != null && !params.getKeyword().isBlank()) {
                countQuery.setParameter("keyword", "%" + params.getKeyword().toLowerCase() + "%");
            }
            if (params.getCategoryId() != null) {
                countQuery.setParameter("categoryId", params.getCategoryId());
            }
            if (params.getStatus() != null) {
                countQuery.setParameter("status", params.getStatus());
            }

            long totalItems = countQuery.getSingleResult();
            int totalPages = (int) Math.ceil((double) totalItems / size);

            return new PaginatedResponses<>(annonces, page, totalPages);

        } finally {
            em.close();
        }
    }




    @Override
    public Annonce findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        return em.find(Annonce.class, id);
    }

    @Override
    public void update(Annonce obj) {
        EntityManager em = JPAUtil.getEntityManager();
        em.merge(obj);
    }

    @Override
    public void delete(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Annonce annonce = em.find(Annonce.class, id);
        if (annonce != null) {
            em.remove(annonce);
        }
    }
}
