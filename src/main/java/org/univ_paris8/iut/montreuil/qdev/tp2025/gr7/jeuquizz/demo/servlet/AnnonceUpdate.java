package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceDao;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/annonce/update")
public class AnnonceUpdate extends HttpServlet {

    private AnnonceDao annonceDAO;

    @Override
    public void init() throws ServletException {
        annonceDAO = new AnnonceDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/annonce/list");
            return;
        }

        int id = Integer.parseInt(idParam);

        try {
            Annonce annonce = annonceDAO.findById(id);
            if (annonce == null) {
                response.sendRedirect(request.getContextPath() + "/annonce/list");
                return;
            }

            request.setAttribute("annonce", annonce);
            request.getRequestDispatcher("/AnnonceUpdate.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la récupération de l'annonce", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");

        if (idParam == null || idParam.isEmpty() ||
                title == null || title.isEmpty() ||
                description == null || description.isEmpty() ||
                adress == null || adress.isEmpty() ||
                mail == null || mail.isEmpty()) {

            request.setAttribute("error", "Tous les champs sont obligatoires !");
            doGet(request, response);
            return;
        }

        int id = Integer.parseInt(idParam);
        Annonce annonce = new Annonce(id, title, description, adress, mail, null);

        try {
            annonceDAO.update(annonce);
            response.sendRedirect(request.getContextPath() + "/annonce/list");
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la mise à jour de l'annonce", e);
        }
    }
}
