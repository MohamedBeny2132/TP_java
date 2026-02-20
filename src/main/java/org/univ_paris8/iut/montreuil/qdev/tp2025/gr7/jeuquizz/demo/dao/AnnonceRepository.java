package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;

import java.util.List;

public class AnnonceRepository extends DAO<Annonce> {

    public AnnonceRepository() {
    }

    @Override
    public void create(Annonce obj, EntityManager em) {
        em.persist(obj);
    }

    @Override
    public List<Annonce> findAll(EntityManager em) {
        // Use JOIN FETCH to avoid LazyInitializationException
        String jpql = "SELECT a FROM Annonce a JOIN FETCH a.author JOIN FETCH a.category ORDER BY a.date DESC";
        TypedQuery<Annonce> query = em.createQuery(jpql, Annonce.class);
        return query.getResultList();
    }

    public PaginatedResponses<Annonce> findAllWithParam(AnnonceSearchParams params, EntityManager em) {

        try {
            // Requête principale pour récupérer les annonces filtrées avec JOIN FETCH
            StringBuilder jpql = new StringBuilder(
                    "SELECT a FROM Annonce a JOIN FETCH a.author JOIN FETCH a.category WHERE 1=1");

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

            // Requête COUNT pour total (pas besoin de JOIN FETCH ici)
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
            // em.close(); NO! Service handles this.
        }
    }

    @Override
    public Annonce findById(int id, EntityManager em) {
        // We can't use em.find() if we want eager fetching for associated entities
        // em.find(Annonce.class, id) might be lazy or not depending on Entity config
        // But to be safe and consistent with findAll, lets use JPQL with JOIN FETCH
        try {
            return em
                    .createQuery("SELECT a FROM Annonce a JOIN FETCH a.author JOIN FETCH a.category WHERE a.id = :id",
                            Annonce.class)
                    .setParameter("id", (long) id) // Casting to long because Annonce ID is Long in Entity but Int in
                                                   // DAO signature... wait, let me check entity
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public void update(Annonce obj, EntityManager em) {
        em.merge(obj);
    }

    @Override
    public void delete(int id, EntityManager em) {
        Annonce annonce = em.find(Annonce.class, id); // find is enough for delete, we just need the reference
        if (annonce != null) {
            em.remove(annonce);
        }
    }
}
