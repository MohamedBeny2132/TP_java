package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import javax.security.auth.callback.*;
import java.io.IOException;

public class JAASCallbackHandler implements CallbackHandler {
    private final String username;
    private final String password;
    private final String token;

    public JAASCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
        this.token = null;
    }

    public JAASCallbackHandler(String token) {
        this.username = null;
        this.password = null;
        this.token = token;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password != null ? password.toCharArray() : null);
            } else if (callback instanceof TextInputCallback) {
                ((TextInputCallback) callback).setText(token);
            } else {
                throw new UnsupportedCallbackException(callback, "Unsupported callback");
            }
        }
    }
}
