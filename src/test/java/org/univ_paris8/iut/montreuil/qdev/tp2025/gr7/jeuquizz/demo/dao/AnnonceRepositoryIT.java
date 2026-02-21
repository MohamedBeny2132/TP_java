package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Niveau 1 – Tests Repository (intégration avec vraie base de données).
 * 
 * ⚠️ Ces tests nécessitent une base PostgreSQL opérationnelle
 * configurée dans persistence.xml (MasterAnnonce / test / test).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnnonceRepositoryIT {

    private static AnnonceRepository annonceRepository;
    private static EntityManager em;

    @BeforeAll
    static void setUp() {
        annonceRepository = new AnnonceRepository();
    }

    @AfterAll
    static void tearDown() {
        // JPAUtil.close() est supprimé car il ferme le EMF statique pour tous les tests
    }

    @Test
    @Order(1)
    @DisplayName("Test Pagination - Count total items and retrieve first page")
    void testPaginationFirstPage() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(5);

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        assertEquals(5, result.getItems().size(), "La première page devrait avoir 5 éléments");
        assertEquals(3, result.getTotalPages(), "Il devrait y avoir 3 pages pour 12 éléments (taille 5)");

        em.close();
    }

    @Test
    @Order(2)
    @DisplayName("Test Pagination - Retrieve last page")
    void testPaginationLastPage() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(3);
        params.setSize(5);

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        assertEquals(2, result.getItems().size(), "La dernière page devrait avoir 2 éléments");

        em.close();
    }

    @Test
    @Order(3)
    @DisplayName("Test Recherche - Par mot-clé 'Peugeot'")
    void testSearchByKeyword() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(10);
        params.setKeyword("Peugeot");

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("Peugeot 208", result.getItems().get(0).getTitle());

        em.close();
    }

    @Test
    @Order(4)
    @DisplayName("Test Filtrage - Par statut DRAFT")
    void testFilterByStatus() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(10);
        params.setStatus(Status.DRAFT);

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        // Dans import.sql il y a exactement 6 DRAFT (ids pairs: 2, 4, 6, 8, 10, 12)
        assertEquals(6, result.getItems().size());

        em.close();
    }

    @Test
    @Order(5)
    @DisplayName("Test CRUD - FindById : retrouver une annonce de l'import.sql")
    void testFindById() {
        EntityManager em = JPAUtil.getEntityManager();

        Annonce found = annonceRepository.findById(1, em);

        assertNotNull(found, "L'annonce 1 devrait exister");
        assertEquals("Peugeot 208", found.getTitle());
        assertNotNull(found.getAuthor());
        assertEquals("testuser", found.getAuthor().getUsername());

        em.close();
    }

    @Test
    @Order(6)
    @DisplayName("Test CRUD - Delete : supprimer une annonce")
    void testDelete() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        annonceRepository.delete(12, em);
        tx.commit();
        em.close();

        EntityManager em2 = JPAUtil.getEntityManager();
        Annonce deleted = annonceRepository.findById(12, em2);
        assertNull(deleted, "L'annonce 12 devrait être supprimée");
        em2.close();
    }
}
