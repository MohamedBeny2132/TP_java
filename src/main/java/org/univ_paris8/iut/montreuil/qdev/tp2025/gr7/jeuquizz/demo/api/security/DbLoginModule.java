package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.Map;

public class DbLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private User authenticatedUser;
    private boolean loginSucceeded = false;

    private AuthService authService = new AuthService();

    public void setAuthService(AuthService authService) {
        this.authService = authService;
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

        NameCallback nameCallback = new NameCallback("Email: ");
        PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);

        try {
            callbackHandler.handle(new Callback[] { nameCallback, passwordCallback });
            String email = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());

            authenticatedUser = authService.login(email, password);

            if (authenticatedUser != null) {
                loginSucceeded = true;
                return true;
            } else {
                throw new LoginException("Échec de l'authentification : email ou mot de passe incorrect.");
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
