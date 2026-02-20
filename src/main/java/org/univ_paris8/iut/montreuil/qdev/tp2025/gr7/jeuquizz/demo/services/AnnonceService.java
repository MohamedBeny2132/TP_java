package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.util.List;

public class AnnonceService {

    private AnnonceRepository annonceRepository;

    public AnnonceService() {
        this.annonceRepository = new AnnonceRepository();
    }

    public void createAnnonce(Annonce annonce) {

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            // Need to reload User and Category if they are detached or just IDs to avoid
            // "detached entity passed to persist"
            // But here we assume Annonce has valid references or we just persist.
            // Actually, if Annonce has a User object that is detached, persist might fail
            // or duplicate.
            // Let's assume the Servlet constructs Annonce with fetched User/Category or
            // just IDs.
            // Ideally we should merge the Author and Category if they are detached.
            // For now, let's just pass to repo.
            if (annonce.getAuthor() != null && annonce.getAuthor().getId() > 0) {
                annonce.setAuthor(em.merge(annonce.getAuthor()));
            }
            if (annonce.getCategory() != null && annonce.getCategory().getId() > 0) {
                annonce.setCategory(em.merge(annonce.getCategory()));
            }

            annonceRepository.create(annonce, em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Annonce> getAllAnnonces() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepository.findAll(em);
        } finally {
            em.close();
        }
    }

    public PaginatedResponses<Annonce> getAllAnnoncesWithParams(AnnonceSearchParams params) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepository.findAllWithParam(params, em);
        } finally {
            em.close();
        }
    }

    public Annonce getAnnonceById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepository.findById(id, em);
        } finally {
            em.close();
        }
    }

    public void updateAnnonce(Annonce annonce) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Annonce existing = em.find(Annonce.class, annonce.getId());
            if (existing == null) {
                // Should probably throw exception or handle error
                return;
            }

            // We need to update fields.
            // If we just call merge(annonce), it works if annonce is detached.
            // But we need to make sure relations are correct.
            if (annonce.getAuthor() != null && annonce.getAuthor().getId() > 0) {
                annonce.setAuthor(em.merge(annonce.getAuthor()));
            }
            if (annonce.getCategory() != null && annonce.getCategory().getId() > 0) {
                annonce.setCategory(em.merge(annonce.getCategory()));
            }

            annonceRepository.update(annonce, em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteAnnonce(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            // Check existence is done in DAO delete usually or here
            annonceRepository.delete(id, em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

}