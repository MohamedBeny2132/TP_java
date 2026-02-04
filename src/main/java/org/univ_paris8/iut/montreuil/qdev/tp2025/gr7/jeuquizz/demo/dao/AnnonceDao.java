package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.ConnectionDB;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnnonceDao extends DAO<Annonce> {

    @Override
    public void create(Annonce annonce) throws SQLException {
        String sql = "INSERT INTO annonce (title, description, adress, mail) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, annonce.getTitle());
            stmt.setString(2, annonce.getDescription());
            stmt.setString(3, annonce.getAdress());
            stmt.setString(4, annonce.getMail());

            stmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Annonce> findAll() throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String sql = "SELECT id, title, description, adress, mail, date FROM annonce ORDER BY date DESC";

        try (Connection conn = ConnectionDB.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                annonces.add(new Annonce(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("adress"),
                        rs.getString("mail"),
                        rs.getTimestamp("date")
                ));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return annonces;
    }

    @Override
    public Annonce findById(int id) throws SQLException {
        String sql = "SELECT id, title, description, adress, mail, date FROM annonce WHERE id = ?";

        try (Connection conn = ConnectionDB.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Annonce(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("adress"),
                            rs.getString("mail"),
                            rs.getTimestamp("date")
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void update(Annonce annonce) throws SQLException {
        String sql = "UPDATE annonce SET title = ?, description = ?, adress = ?, mail = ? WHERE id = ?";

        try (Connection conn = ConnectionDB.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, annonce.getTitle());
            stmt.setString(2, annonce.getDescription());
            stmt.setString(3, annonce.getAdress());
            stmt.setString(4, annonce.getMail());
            stmt.setInt(5, annonce.getId());

            stmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM annonce WHERE id = ?";

        try (Connection conn = ConnectionDB.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
