package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/annonce/add")
public class AnnonceAdd extends HttpServlet {

    private AnnonceDao annonceDAO;

    @Override
    public void init() throws ServletException {
        annonceDAO = new AnnonceDao();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/AnnonceAdd.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");

        if (title == null || title.isEmpty() ||
                description == null || description.isEmpty() ||
                adress == null || adress.isEmpty() ||
                mail == null || mail.isEmpty()) {

            request.setAttribute("error", "Tous les champs sont obligatoires !");
            request.getRequestDispatcher("/AnnonceAdd.jsp").forward(request, response);
            return;
        }

        Annonce annonce = new Annonce(title, description, adress, mail);

        try {
            annonceDAO.create(annonce);

            response.sendRedirect(request.getContextPath() + "/annonce/add");

        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'ajout de l'annonce en base", e);
        }
    }
}
