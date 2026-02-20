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
class AnnonceRepositoryTest {

    private static AnnonceRepository annonceRepository;
    private static UserRepository userRepository;
    private static CategoryRepository categoryRepository;

    private static EntityManager em;

    // IDs partagés entre les tests
    private static int annonceId;
    private static User testUser;
    private static Category testCategory;

    @BeforeAll
    static void setUp() {
        annonceRepository = new AnnonceRepository();
        userRepository = new UserRepository();
        categoryRepository = new CategoryRepository();

        // Créer un utilisateur et une catégorie de test
        em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        testUser = new User("repo_testuser_" + System.currentTimeMillis(),
                "repotest" + System.currentTimeMillis() + "@test.com",
                "hashed_password_123");
        userRepository.create(testUser, em);

        testCategory = new Category("Catégorie Test " + System.currentTimeMillis());
        categoryRepository.create(testCategory, em);

        tx.commit();
        em.close();
    }

    @AfterAll
    static void tearDown() {
        JPAUtil.close();
    }

    // ===========================
    // Tests CRUD
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test CRUD – Create : une annonce peut être persistée")
    void testCreate() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        Annonce annonce = new Annonce("Appart à louer",
                "Beau appartement au centre", "Paris 75001", "test@mail.com");
        annonce.setAuthor(em.merge(testUser));
        annonce.setCategory(em.merge(testCategory));

        tx.begin();
        annonceRepository.create(annonce, em);
        tx.commit();

        annonceId = annonce.getId();
        assertTrue(annonceId > 0, "L'annonce devrait avoir un ID généré > 0");
        em.close();
    }

    @Test
    @Order(2)
    @DisplayName("Test CRUD – FindById : retrouver l'annonce créée")
    void testFindById() {
        EntityManager em = JPAUtil.getEntityManager();

        Annonce found = annonceRepository.findById(annonceId, em);

        assertNotNull(found, "L'annonce devrait exister");
        assertEquals("Appart à louer", found.getTitle());
        // JOIN FETCH garantit que les relations sont chargées même après fermeture EM
        assertNotNull(found.getAuthor(), "L'auteur devrait être chargé (JOIN FETCH)");
        assertNotNull(found.getCategory(), "La catégorie devrait être chargée (JOIN FETCH)");

        em.close();
    }

    @Test
    @Order(3)
    @DisplayName("Test CRUD – FindAll : la liste contient l'annonce créée")
    void testFindAll() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Annonce> annonces = annonceRepository.findAll(em);

        assertNotNull(annonces);
        assertFalse(annonces.isEmpty(), "La liste ne devrait pas être vide");
        // Vérifier que notre annonce est bien dans la liste
        boolean found = annonces.stream().anyMatch(a -> a.getId() == annonceId);
        assertTrue(found, "L'annonce créée devrait être dans la liste complète");

        em.close();
    }

    @Test
    @Order(4)
    @DisplayName("Test CRUD – Update : modifier le titre d'une annonce")
    void testUpdate() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        Annonce toUpdate = annonceRepository.findById(annonceId, em);
        assertNotNull(toUpdate);
        toUpdate.setTitle("Appart MODIFIÉ");

        tx.begin();
        annonceRepository.update(toUpdate, em);
        tx.commit();
        em.close();

        // Vérifier dans un nouveau EM
        EntityManager em2 = JPAUtil.getEntityManager();
        Annonce updated = annonceRepository.findById(annonceId, em2);
        assertEquals("Appart MODIFIÉ", updated.getTitle(), "Le titre devrait avoir été modifié");
        em2.close();
    }

    // ===========================
    // Tests de recherche et pagination
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test Pagination – findAllWithParam retourne une page correcte")
    void testPagination() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(5);

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        assertNotNull(result.getItems());
        assertTrue(result.getItems().size() <= 5, "La page ne devrait pas dépasser 5 éléments");
        assertTrue(result.getTotalPages() >= 1, "Il devrait y avoir au moins 1 page");

        em.close();
    }

    @Test
    @Order(6)
    @DisplayName("Test Recherche – recherche par mot-clé 'MODIFIÉ'")
    void testSearchByKeyword() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(10);
        params.setKeyword("MODIFIÉ");

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        assertFalse(result.getItems().isEmpty(), "La recherche par mot-clé devrait retourner des résultats");
        assertTrue(result.getItems().stream()
                .anyMatch(a -> a.getTitle().contains("MODIFIÉ")),
                "L'annonce modifiée devrait apparaître dans les résultats");

        em.close();
    }

    @Test
    @Order(7)
    @DisplayName("Test Filtrage – filtrer par statut DRAFT")
    void testFilterByStatus() {
        EntityManager em = JPAUtil.getEntityManager();

        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(10);
        params.setStatus(Status.DRAFT);

        PaginatedResponses<Annonce> result = annonceRepository.findAllWithParam(params, em);

        assertNotNull(result);
        // Toutes les annonces retournées doivent être DRAFT
        result.getItems().forEach(a -> assertEquals(Status.DRAFT, a.getStatus(),
                "Toutes les annonces filtrées devraient être en DRAFT"));

        em.close();
    }

    @Test
    @Order(8)
    @DisplayName("Test CRUD – Delete : supprimer l'annonce de test")
    void testDelete() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        annonceRepository.delete(annonceId, em);
        tx.commit();
        em.close();

        // Vérifier la suppression dans un nouveau EM
        EntityManager em2 = JPAUtil.getEntityManager();
        Annonce deleted = annonceRepository.findById(annonceId, em2);
        assertNull(deleted, "L'annonce supprimée ne devrait plus exister");
        em2.close();
    }
}
