package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Status;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/annonce/list")
public class AnnonceList extends HttpServlet {

    private AnnonceService annonceService;

    @Override
    public void init() throws ServletException {
        annonceService = new AnnonceService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        AnnonceSearchParams params = new AnnonceSearchParams();

        // Pagination par défaut
        int page = 1;
        int size = 5;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        String sizeParam = request.getParameter("size");
        if (sizeParam != null && !sizeParam.isEmpty()) {
            size = Integer.parseInt(sizeParam);
        }

        params.setPage(page);
        params.setSize(size);

        // Filtres
        params.setKeyword(request.getParameter("keyword"));

        String statusParam = request.getParameter("status");
        if (statusParam != null && !statusParam.isEmpty()) {
            params.setStatus(Status.valueOf(statusParam));
        }

        String categoryId = request.getParameter("categoryId");
        if (categoryId != null && !categoryId.isEmpty()) {
            params.setCategoryId(Long.parseLong(categoryId));
        }

        // Récupération paginée
        PaginatedResponses<Annonce> paginatedAnnonces = annonceService.getAllAnnoncesWithParams(params);

        // Envoi vers JSP - on envoie l'objet complet
        request.setAttribute("paginatedAnnonces", paginatedAnnonces);
        request.setAttribute("params", params);

        request.getRequestDispatcher("/jsp/AnnonceList.jsp").forward(request, response);
    }
}