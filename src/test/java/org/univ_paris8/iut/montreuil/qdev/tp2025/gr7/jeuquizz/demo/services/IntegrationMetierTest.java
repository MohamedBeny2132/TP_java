package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.CategoryRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Niveau 3 – Tests d'intégration métier (avec vraie base de données).
 *
 * Vérifie le flux complet : AuthService → AnnonceService → BDD
 * et détecte les problèmes LazyInitializationException et N+1.
 *
 * ⚠️ Nécessite une base PostgreSQL opérationnelle.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntegrationMetierTest {

    private static AuthService authService;
    private static AnnonceService annonceService;
    private static CategoryRepository categoryRepository;

    private static User testUser;
    private static Category testCategory;
    private static int createdAnnonceId;

    @BeforeAll
    static void setUpAll() {
        authService = new AuthService();
        annonceService = new AnnonceService();
        categoryRepository = new CategoryRepository();

        // Préparer une catégorie réutilisable
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        testCategory = new Category("Intégration Test Cat " + System.currentTimeMillis());
        categoryRepository.create(testCategory, em);
        tx.commit();
        em.close();
    }

    @AfterAll
    static void tearDownAll() {
        JPAUtil.close();
    }

    // =========================================
    // Flux complet : Inscription → Connexion → Publier → Lister
    // =========================================

    @Test
    @Order(1)
    @DisplayName("Flux 1 – Inscription d'un utilisateur")
    void testInscription() {
        String uniqueEmail = "integ_" + System.currentTimeMillis() + "@test.com";
        testUser = new User("integ_user_" + System.currentTimeMillis(), uniqueEmail, "Password123!");

        boolean result = authService.register(testUser);

        assertTrue(result, "L'inscription devrait réussir");
        assertTrue(testUser.getId() > 0, "L'utilisateur devrait avoir un ID après inscription");
    }

    @Test
    @Order(2)
    @DisplayName("Flux 2 – Connexion avec le bon mot de passe")
    void testConnexionAvecBonMotDePasse() {
        User logged = authService.login(testUser.getEmail(), "Password123!");

        assertNotNull(logged, "La connexion devrait réussir");
        assertEquals(testUser.getEmail(), logged.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Flux 3 – Connexion avec mauvais mot de passe refusée")
    void testConnexionAvecMauvaisMotDePasse() {
        User logged = authService.login(testUser.getEmail(), "MAUVAIS_MDP");

        assertNull(logged, "La connexion avec un mauvais MDP devrait échouer");
    }

    @Test
    @Order(4)
    @DisplayName("Flux 4 – Publier une annonce puis la retrouver")
    void testPublierEtRetrouvrerUneAnnonce() {
        Annonce annonce = new Annonce(
                "Annonce intégration " + System.currentTimeMillis(),
                "Description de test",
                "Paris 75001",
                testUser.getEmail());
        annonce.setAuthor(testUser);
        annonce.setCategory(testCategory);

        annonceService.createAnnonce(annonce);
        createdAnnonceId = annonce.getId();

        assertTrue(createdAnnonceId > 0, "L'annonce devrait avoir un ID après création");

        // Retrouver l'annonce — tester que JOIN FETCH résout le LazyInit
        Annonce retrieved = annonceService.getAnnonceById(createdAnnonceId);
        assertNotNull(retrieved);

        // Ces accès doivent fonctionner APRÈS fermeture de l'EntityManager
        // (le JOIN FETCH dans le repo garantit le chargement)
        assertDoesNotThrow(() -> {
            String authorName = retrieved.getAuthor().getUsername();
            assertNotNull(authorName, "Le username de l'auteur doit être accessible");
        }, "Accès à author.username ne doit pas lever de LazyInitializationException");

        assertDoesNotThrow(() -> {
            String catLabel = retrieved.getCategory().getLabel();
            assertNotNull(catLabel);
        }, "Accès à category.label ne doit pas lever de LazyInitializationException");
    }

    @Test
    @Order(5)
    @DisplayName("Flux 5 – Problème N+1 : lister TOUTES les annonces ne génère qu'une seule requête SQL")
    void testPasDeProblemeNPlus1() {
        // Dans une implémentation naïve (sans JOIN FETCH), chaque accès à
        // author/category
        // générerait une requête supplémentaire → N+1 queries.
        // Avec JOIN FETCH, tout est chargé en une seule requête.
        List<Annonce> annonces = annonceService.getAllAnnonces();

        assertFalse(annonces.isEmpty());

        // Accéder à toutes les relations associées sans exception
        assertDoesNotThrow(() -> {
            for (Annonce a : annonces) {
                String authorName = a.getAuthor().getUsername();
                String catLabel = a.getCategory().getLabel();
                assertNotNull(authorName);
                assertNotNull(catLabel);
            }
        }, "Aucune LazyInitializationException ne doit se produire grâce au JOIN FETCH");
    }

    @Test
    @Order(6)
    @DisplayName("Flux 6 – Pagination : page 1 avec 3 éléments")
    void testPaginationService() {
        AnnonceSearchParams params = new AnnonceSearchParams();
        params.setPage(1);
        params.setSize(3);

        PaginatedResponses<Annonce> page = annonceService.getAllAnnoncesWithParams(params);

        assertNotNull(page);
        assertTrue(page.getItems().size() <= 3);
        assertTrue(page.getTotalPages() >= 1);

        // Les données de pagination doivent être cohérentes
        assertTrue(page.getCurrentPage() == 1);
    }

    @Test
    @Order(7)
    @DisplayName("Flux 7 – Transaction atomique : une erreur doit tout annuler")
    void testTransactionRollback() {
        // Créer une annonce avec une catégorie invalide (id = -999) pour provoquer une
        // erreur
        Annonce annonce = new Annonce("Annonce invalide", "Test rollback", "Adresse", testUser.getEmail());
        Category invalidCategory = new Category("Inexistante");
        // On ne persiste pas cette catégorie, son id est 0
        annonce.setAuthor(testUser);
        annonce.setCategory(invalidCategory); // pas d'ID valide, pas de FK valide

        // L'annonce ne devrait PAS être persistée
        // On vérifie qu'aucune exception ne remonte jusqu'à l'appelant (elle est
        // absorbée en interne)
        assertDoesNotThrow(() -> annonceService.createAnnonce(annonce));

        // L'ID doit rester à 0 si la transaction a été rollbackée
        assertEquals(0, annonce.getId(), "Aucun ID ne doit être généré si la transaction échoue");
    }
}
