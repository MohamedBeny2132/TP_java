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

        transaction.begin();
        annonceRepository.create(annonce);
        transaction.commit();
        em.close();
    }


    public List<Annonce> getAllAnnonces() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Annonce> annonces = annonceRepository.findAll();
        em.close();
        return annonces;
    }

    public PaginatedResponses<Annonce> getAllAnnoncesWithParams(AnnonceSearchParams params) {
        EntityManager em = JPAUtil.getEntityManager();
        PaginatedResponses<Annonce> annonces = annonceRepository.findAllWithParam(params);
        em.close();
        return annonces;
    }




    public Annonce getAnnonceById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        Annonce annonce = annonceRepository.findById(id);
        em.close();

        if (annonce == null) {
            //
        }

        return annonce;
    }


    public void updateAnnonce(Annonce annonce)  {
        if (annonce.getId() == 0) {
            //
        }


        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Annonce existing = em.find(Annonce.class, annonce.getId());
        if (existing == null) {
            transaction.rollback();
            em.close();
        }

        annonceRepository.update(annonce);
        transaction.commit();
        em.close();
    }


    public void deleteAnnonce(int id)  {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        Annonce annonce = em.find(Annonce.class, id);
        if (annonce == null) {
            transaction.rollback();
            em.close();
        }

        annonceRepository.delete(id);
        transaction.commit();
        em.close();
    }


}