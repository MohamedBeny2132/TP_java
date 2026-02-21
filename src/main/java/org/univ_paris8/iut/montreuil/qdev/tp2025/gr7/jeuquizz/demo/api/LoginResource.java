package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.LoginRequest;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.TokenResponse;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security.JAASCallbackHandler;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.security.UserPrincipal;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.TokenService;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    private TokenService tokenService = new TokenService();

    @POST
    public Response login(@Valid LoginRequest loginRequest) {
        try {
            LoginContext lc = new LoginContext("MasterAnnonceLogin",
                    new JAASCallbackHandler(loginRequest.getEmail(), loginRequest.getPassword()));
            lc.login();

            java.util.Set<UserPrincipal> principals = lc.getSubject().getPrincipals(UserPrincipal.class);
            if (principals.isEmpty()) {
                throw new LoginException("Échec de la récupération du Principal");
            }

            User user = principals.iterator().next().getUser();
            String token = tokenService.generateToken(user);

            return Response.ok(new TokenResponse(token, 3600, user.getUsername())).build();

        } catch (LoginException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Email ou mot de passe incorrect : " + e.getMessage()))
                    .build();
        }
    }
}
