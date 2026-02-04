package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/annonce/delete")
public class AnnonceDelete extends HttpServlet {

    private AnnonceDao annonceDAO;

    @Override
    public void init() throws ServletException {
        annonceDAO = new AnnonceDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            int id = Integer.parseInt(idParam);
            try {
                annonceDAO.delete(id);
            } catch (SQLException e) {
                throw new ServletException("Erreur lors de la suppression de l'annonce", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/annonce/list");
    }
}
