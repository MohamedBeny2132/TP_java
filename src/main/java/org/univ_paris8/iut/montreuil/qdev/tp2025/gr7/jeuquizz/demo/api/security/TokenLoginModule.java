package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.TokenService;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class TokenLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private User authenticatedUser;
    private boolean loginSucceeded = false;

    private TokenService tokenService = new TokenService();

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState,
            Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Error: no CallbackHandler available");
        }

        TextInputCallback tokenCallback = new TextInputCallback("Token: ");

        try {
            callbackHandler.handle(new Callback[] { tokenCallback });
            String token = tokenCallback.getText();

            if (token == null || token.isEmpty()) {
                throw new LoginException("Jeton manquant.");
            }

            authenticatedUser = tokenService.validateToken(token);

            if (authenticatedUser != null) {
                loginSucceeded = true;
                return true;
            } else {
                throw new LoginException("Jeton invalide ou expiré.");
            }
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        if (loginSucceeded) {
            UserPrincipal principal = new UserPrincipal(authenticatedUser);
            RolePrincipal rolePrincipal = new RolePrincipal("ROLE_USER");

            if (!subject.getPrincipals().contains(principal)) {
                subject.getPrincipals().add(principal);
            }
            if (!subject.getPrincipals().contains(rolePrincipal)) {
                subject.getPrincipals().add(rolePrincipal);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean abort() throws LoginException {
        loginSucceeded = false;
        authenticatedUser = null;
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().removeIf(p -> p instanceof UserPrincipal);
        loginSucceeded = false;
        authenticatedUser = null;
        return true;
    }
}
