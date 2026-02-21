package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String username;
    private final long userId;
    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
        this.username = user.getUsername();
        this.userId = user.getId();
    }

    @Override
    public String getName() {
        return username;
    }

    public long getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }
}
