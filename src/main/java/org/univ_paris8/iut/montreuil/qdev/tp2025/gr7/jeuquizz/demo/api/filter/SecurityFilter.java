package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.filter;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security.JAASCallbackHandler;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security.RolePrincipal;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security.UserPrincipal;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Récupère le jeton dans l'en-tête Authorization (ex: "Bearer <token>")
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Jeton de sécurité manquant ou invalide")
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            javax.security.auth.login.LoginContext lc = new javax.security.auth.login.LoginContext("MasterAnnonceToken",
                    new JAASCallbackHandler(token));
            lc.login();

            java.util.Set<UserPrincipal> principals = lc.getSubject().getPrincipals(UserPrincipal.class);
            if (principals.isEmpty()) {
                throw new javax.security.auth.login.LoginException("Erreur de reconstruction d'identité");
            }

            User user = principals.iterator().next().getUser();

            // Définit le contexte de sécurité JAX-RS
            requestContext.setSecurityContext(new javax.ws.rs.core.SecurityContext() {
                @Override
                public java.security.Principal getUserPrincipal() {
                    return () -> user.getUsername();
                }

                @Override
                public boolean isUserInRole(String role) {
                    return lc.getSubject().getPrincipals(RolePrincipal.class).stream()
                            .anyMatch(p -> p.getName().equals(role));
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

            // Stocke l'utilisateur dans les propriétés pour un accès facile par @Context
            requestContext.setProperty("user", user);

        } catch (javax.security.auth.login.LoginException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Jeton de sécurité expiré ou invalide : " + e.getMessage())
                    .build());
        }
    }
}
