package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepository();
    }


    public User login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        // Vérifier le mot de passe avec BCrypt
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }


    public boolean register(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return false;
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        try {
            userRepository.create(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





}