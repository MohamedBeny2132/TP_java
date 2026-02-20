package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {

    public abstract void create(T obj, EntityManager em) throws SQLException;

    public abstract List<T> findAll(EntityManager em) throws SQLException;

    public abstract T findById(int id, EntityManager em) throws SQLException;

    public abstract void update(T obj, EntityManager em) throws SQLException;

    public abstract void delete(int id, EntityManager em) throws SQLException;
}
