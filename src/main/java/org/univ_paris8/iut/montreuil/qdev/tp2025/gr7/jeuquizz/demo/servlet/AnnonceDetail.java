package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/annonce/detail")
public class AnnonceDetail extends HttpServlet {

    private AnnonceService annonceService;

    @Override
    public void init() throws ServletException {
        annonceService = new AnnonceService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/annonce/list");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            Annonce annonce = annonceService.getAnnonceById(id);

            if (annonce == null) {
                request.setAttribute("error", "Annonce introuvable");
            } else {
                request.setAttribute("annonce", annonce);
            }

            request.getRequestDispatcher("/jsp/AnnonceDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/annonce/list");
        }
    }
}
