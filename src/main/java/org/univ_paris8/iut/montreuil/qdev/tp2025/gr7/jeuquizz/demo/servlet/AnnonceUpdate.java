package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/annonce/update")
public class AnnonceUpdate extends HttpServlet {

    private AnnonceService annonceService;

    @Override
    public void init() throws ServletException {
        annonceService = new AnnonceService();
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
        Annonce annonce = annonceService.getAnnonceById(id);

        if (annonce == null) {
            response.sendRedirect(request.getContextPath() + "/annonce/list");
            return;
        }

        request.setAttribute("annonce", annonce);
        request.getRequestDispatcher("/jsp/AnnonceUpdate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");

        int id = Integer.parseInt(idParam);
        Annonce annonce = new Annonce(id, title, description, adress, mail, null);

        annonceService.updateAnnonce(annonce);
        response.sendRedirect(request.getContextPath() + "/annonce/list");
    }
}