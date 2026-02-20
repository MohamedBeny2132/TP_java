package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class AuthService {

    private UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    public User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        EntityManager em = org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil.getEntityManager();
        try {
            User user = userRepository.findByEmail(email, em);

            if (user == null) {
                return null;
            }

            // Vérifier le mot de passe avec BCrypt
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }

            return null;
        } finally {
            em.close();
        }
    }

    public boolean register(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return false;
        }

        EntityManager em = org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.utils.JPAUtil.getEntityManager();
        jakarta.persistence.EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User existingUser = userRepository.findByEmail(user.getEmail(), em);
            if (existingUser != null) {
                return false;
            }

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);

            userRepository.create(user, em);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}