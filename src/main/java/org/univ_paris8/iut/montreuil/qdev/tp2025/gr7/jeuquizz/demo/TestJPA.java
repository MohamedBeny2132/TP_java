package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Category;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.CategoryRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import jakarta.persistence.EntityManager;
import java.sql.SQLException;
import java.util.List;

public class TestJPA {

    public static void main(String[] args) {
        System.out.println("Début du test JPA...");

        AuthService authService = new AuthService();
        AnnonceService annonceService = new AnnonceService();
        CategoryRepository categoryRepository = new CategoryRepository();

        // 1. Créer un utilisateur
        User user = new User("testuser_" + System.currentTimeMillis(),
                "test" + System.currentTimeMillis() + "@test.com", "password123");
        boolean registered = authService.register(user);
        System.out.println("Utilisateur enregistré : " + registered);

        if (registered) {
            User loggedUser = authService.login(user.getEmail(), "password123");
            System.out.println("Utilisateur connecté : " + (loggedUser != null ? loggedUser.getUsername() : "Echec"));

            if (loggedUser != null) {
                // 2. Créer une catégorie (besoin d'un EM pour ça ou d'un service category, on
                // va le faire "à la main" via le repo et JPAUtil pour le test)
                EntityManager em = JPAUtil.getEntityManager();
                em.getTransaction().begin();
                Category cat = new Category();
                cat.setLabel("Immobilier " + System.currentTimeMillis());
                categoryRepository.create(cat, em);
                em.getTransaction().commit();
                em.close();
                System.out.println("Catégorie créée : " + cat.getLabel() + " (ID: " + cat.getId() + ")");

                // 3. Créer une annonce
                Annonce annonce = new Annonce("Super Appart", "Un super appart à louer", "Paris", "contact@appart.com");
                annonce.setAuthor(loggedUser);
                annonce.setCategory(cat);

                annonceService.createAnnonce(annonce);
                System.out.println("Annonce créée avec ID : " + annonce.getId());

                // 4. Vérifier la récupération
                Annonce retrieved = annonceService.getAnnonceById(annonce.getId());
                System.out.println("Annonce récupérée : " + (retrieved != null ? retrieved.getTitle() : "Non trouvée"));
                if (retrieved != null) {
                    System.out.println("Auteur : " + retrieved.getAuthor().getUsername());
                    System.out.println("Catégorie : " + retrieved.getCategory().getLabel());
                }

                // 5. Lister tout
                List<Annonce> all = annonceService.getAllAnnonces();
                System.out.println("Nombre total d'annonces : " + all.size());
            }
        }

        JPAUtil.close();
        System.out.println("Fin du test.");
    }
}
