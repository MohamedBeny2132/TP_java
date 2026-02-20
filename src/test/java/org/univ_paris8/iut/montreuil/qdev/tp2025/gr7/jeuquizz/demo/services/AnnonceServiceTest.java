package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Niveau 2 – Tests Service (unitaires avec Mockito).
 *
 * Les repositories et l'EntityManager sont mockés —
 * pas besoin de vraie base de données.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class AnnonceServiceTest {

    // Mock du repository
    @Mock
    private AnnonceRepository annonceRepository;

    // Mock de l'EntityManager et Transaction
    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction entityTransaction;

    // Objet à tester — on l'injecte manuellement pour contrôler ses dépendances
    private AnnonceService annonceService;

    // On intercepte les appels à JPAUtil.getEntityManager()
    private static MockedStatic<JPAUtil> jpaMocked;

    @BeforeAll
    static void initJpaMock() {
        jpaMocked = mockStatic(JPAUtil.class);
    }

    @AfterAll
    static void closeJpaMock() {
        jpaMocked.close();
    }

    @BeforeEach
    void setUp() {
        // Faire en sorte que JPAUtil retourne notre mock EM
        jpaMocked.when(JPAUtil::getEntityManager).thenReturn(entityManager);
        lenient().when(entityManager.getTransaction()).thenReturn(entityTransaction);
        lenient().when(entityManager.find(eq(Annonce.class), anyInt())).thenReturn(null);

        // Injecter manuellement le repository mocké dans le service
        annonceService = new AnnonceService();
        // On injecte le mock via réflexion (le champ est privé)
        try {
            java.lang.reflect.Field repoField = AnnonceService.class.getDeclaredField("annonceRepository");
            repoField.setAccessible(true);
            repoField.set(annonceService, annonceRepository);
        } catch (Exception e) {
            fail("Impossible d'injecter le mock : " + e.getMessage());
        }
    }

    // ===========================
    // Tests règles métier
    // ===========================

    @Test
    @DisplayName("createAnnonce – devrait appeler repo.create et committer la transaction")
    void testCreateAnnonce_Nominal() {
        Annonce annonce = buildTestAnnonce();

        // Simuler merge des entités liées
        when(entityManager.merge(any(User.class))).thenReturn(annonce.getAuthor());
        when(entityManager.merge(any(Category.class))).thenReturn(annonce.getCategory());

        annonceService.createAnnonce(annonce);

        // Vérifier que la transaction est bien gérée
        verify(entityTransaction).begin();
        verify(annonceRepository).create(eq(annonce), eq(entityManager));
        verify(entityTransaction).commit();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("createAnnonce – devrait rollback en cas d'exception")
    void testCreateAnnonce_RollbackOnException() {
        Annonce annonce = buildTestAnnonce();

        when(entityManager.merge(any(User.class))).thenReturn(annonce.getAuthor());
        when(entityManager.merge(any(Category.class))).thenReturn(annonce.getCategory());

        // Simuler une erreur dans le repository
        doThrow(new RuntimeException("Erreur BDD simulée"))
                .when(annonceRepository).create(any(), any());
        when(entityTransaction.isActive()).thenReturn(true);

        annonceService.createAnnonce(annonce);

        // Vérifier que rollback est appelé
        verify(entityTransaction).rollback();
        verify(entityManager).close(); // EM doit toujours être fermé
    }

    @Test
    @DisplayName("getAllAnnonces – devrait retourner la liste du repository")
    void testGetAllAnnonces() {
        List<Annonce> fakeList = Arrays.asList(buildTestAnnonce(), buildTestAnnonce());
        when(annonceRepository.findAll(entityManager)).thenReturn(fakeList);

        List<Annonce> result = annonceService.getAllAnnonces();

        assertEquals(2, result.size());
        verify(annonceRepository).findAll(entityManager);
        verify(entityManager).close();
    }

    @Test
    @DisplayName("getAnnonceById – devrait appeler repo.findById avec le bon ID")
    void testGetAnnonceById() {
        Annonce expected = buildTestAnnonce();
        when(annonceRepository.findById(42, entityManager)).thenReturn(expected);

        Annonce result = annonceService.getAnnonceById(42);

        assertNotNull(result);
        assertEquals(expected.getTitle(), result.getTitle());
        verify(annonceRepository).findById(42, entityManager);
    }

    @Test
    @DisplayName("updateAnnonce – devrait ignorer si l'annonce n'existe pas")
    void testUpdateAnnonce_NotFound() {
        Annonce annonce = buildTestAnnonce();
        // em.find retourne null → l'annonce n'existe pas
        when(entityManager.find(Annonce.class, annonce.getId())).thenReturn(null);

        annonceService.updateAnnonce(annonce);

        // Ne devrait PAS appeler update si l'entité n'existe pas
        verify(annonceRepository, never()).update(any(), any());
        // Mais doit quand même fermer l'EM et rollbacker si actif
        verify(entityManager).close();
    }

    @Test
    @DisplayName("deleteAnnonce – devrait appeler repo.delete et committer")
    void testDeleteAnnonce() {
        annonceService.deleteAnnonce(5);

        verify(entityTransaction).begin();
        verify(annonceRepository).delete(5, entityManager);
        verify(entityTransaction).commit();
        verify(entityManager).close();
    }

    // ===========================
    // Helpers
    // ===========================

    private Annonce buildTestAnnonce() {
        User user = new User("testuser", "test@test.com", "pass");
        Category cat = new Category("Immobilier");

        Annonce a = new Annonce("Titre test", "Description test", "Paris", "mail@test.com");
        a.setAuthor(user);
        a.setCategory(cat);
        return a;
    }
}
