package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class Register extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }

    /** GET /register → affiche le formulaire d'inscription */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Si déjà connecté → rediriger
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("id") != null) {
            response.sendRedirect(request.getContextPath() + "/annonce/list");
            return;
        }

        request.getRequestDispatcher("/jsp/Register.jsp").forward(request, response);
    }

    /** POST /register → valide et crée le compte */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        // ── Validation ──────────────────────────────────────────────────────
        if (isEmpty(username) || isEmpty(email) || isEmpty(password) || isEmpty(confirm)) {
            request.setAttribute("error", "Veuillez remplir tous les champs.");
            request.getRequestDispatcher("/jsp/Register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirm)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/jsp/Register.jsp").forward(request, response);
            return;
        }

        if (password.length() < 6) {
            request.setAttribute("error", "Le mot de passe doit contenir au moins 6 caractères.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/jsp/Register.jsp").forward(request, response);
            return;
        }

        // ── Inscription ──────────────────────────────────────────────────────
        User user = new User(username.trim(), email.trim(), password);
        boolean success = authService.register(user);

        if (!success) {
            request.setAttribute("error", "Cet email est déjà utilisé. Essayez de vous connecter.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/jsp/Register.jsp").forward(request, response);
            return;
        }

        // ── Connexion automatique après inscription ───────────────────────────
        HttpSession session = request.getSession(true);
        session.setAttribute("id", user.getId());
        session.setAttribute("username", user.getUsername());

        response.sendRedirect(request.getContextPath() + "/annonce/list");
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
