package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Category;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.CategoryRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.UserRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jakarta.persistence.EntityManager;
import java.io.IOException;

@WebServlet("/annonce/add")
public class AnnonceAdd extends HttpServlet {

    private AnnonceService annonceService;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Override
    public void init() throws ServletException {
        annonceService = new AnnonceService();
        userRepository = new UserRepository();
        categoryRepository = new CategoryRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Charger les catégories pour le formulaire
        EntityManager em = JPAUtil.getEntityManager();
        try {
            request.setAttribute("categories", categoryRepository.findAll(em));
        } finally {
            em.close();
        }

        request.getRequestDispatcher("/jsp/AnnonceAdd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Récupérer les paramètres du formulaire
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");
        String categoryIdStr = request.getParameter("categoryId");

        // 2. Validation basique
        if (title == null || title.trim().isEmpty() ||
                description == null || description.trim().isEmpty() ||
                adress == null || adress.trim().isEmpty() ||
                categoryIdStr == null || categoryIdStr.trim().isEmpty()) {

            request.setAttribute("error", "Veuillez remplir tous les champs obligatoires.");

            EntityManager em = JPAUtil.getEntityManager();
            try {
                request.setAttribute("categories", categoryRepository.findAll(em));
            } finally {
                em.close();
            }

            request.getRequestDispatcher("/jsp/AnnonceAdd.jsp").forward(request, response);
            return;
        }

        // 3. Récupérer l'utilisateur connecté depuis la session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        long userId = (long) session.getAttribute("id");

        // 4. Charger l'utilisateur et la catégorie depuis la BDD
        EntityManager em = JPAUtil.getEntityManager();
        User author;
        Category category;
        try {
            author = em.find(User.class, userId);
            category = em.find(Category.class, Long.parseLong(categoryIdStr));
        } finally {
            em.close();
        }

        if (author == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (category == null) {
            request.setAttribute("error", "Catégorie invalide.");
            request.getRequestDispatcher("/jsp/AnnonceAdd.jsp").forward(request, response);
            return;
        }

        // 5. Construire et persister l'annonce
        Annonce annonce = new Annonce(title, description, adress,
                mail != null ? mail : author.getEmail());
        annonce.setAuthor(author);
        annonce.setCategory(category);

        annonceService.createAnnonce(annonce);
        response.sendRedirect(request.getContextPath() + "/annonce/list");
    }
}