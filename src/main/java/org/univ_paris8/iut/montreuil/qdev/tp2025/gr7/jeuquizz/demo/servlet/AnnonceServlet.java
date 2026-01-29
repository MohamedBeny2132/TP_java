package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AnnonceServlet", value = "/annonce-servlet")
public class AnnonceServlet extends HttpServlet {

    private static final String URL = "jdbc:postgresql://localhost:5432/MasterAnnonce";
    private static final String USER = "test";
    private static final String PASSWORD = "test";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String adress = request.getParameter("adress");
        String mail = request.getParameter("mail");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (title == null || description == null || adress == null || mail == null
                || title.isEmpty() || description.isEmpty() || adress.isEmpty() || mail.isEmpty()) {
            out.println("<h1>Erreur : tous les champs sont requis !</h1>");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO annonce (title, description, adress, mail) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setString(3, adress);
                stmt.setString(4, mail);
                stmt.executeUpdate();
            }

            out.println("<h1>Annonce ajoutée avec succès !</h1>");
            out.println("<p>Title: " + title + "</p>");
            out.println("<p>Description: " + description + "</p>");
            out.println("<p>Adress: " + adress + "</p>");
            out.println("<p>Mail: " + mail + "</p>");

        } catch (SQLException e) {
            e.printStackTrace(out);
            out.println("<h1>Erreur lors de l'insertion dans la base de données</h1>");
        }
    }
}
