package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.LoginRequest;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.TokenResponse;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AuthService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URL;

public class JAASAuthIT extends JerseyTest {

    @BeforeClass
    public static void setUpClass() {
        // Configure JAAS config property for the test environment
        URL resource = JAASAuthIT.class.getClassLoader().getResource("jaas.conf");
        if (resource != null) {
            System.setProperty("java.security.auth.login.config", resource.toExternalForm());
        }
    }

    @Override
    protected Application configure() {
        // Utilise la config réelle ou s'en rapproche
        ResourceConfig config = new ResourceConfig();
        config.packages("org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api");
        config.register(JacksonFeature.class);
        return config;
    }

    @Test
    public void testFullAuthFlow() {
        // 1. Préparer un utilisateur en base (via AuthService)
        AuthService authService = new AuthService();
        String email = "jaas_test_" + System.currentTimeMillis() + "@test.com";
        User user = new User("jaas_user", email, "secret123");
        authService.register(user);

        // 2. Login -> Récupérer token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword("secret123");

        Response loginResponse = target("/login").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loginRequest, MediaType.APPLICATION_JSON));

        Assert.assertEquals(200, loginResponse.getStatus());
        TokenResponse tokenResp = loginResponse.readEntity(TokenResponse.class);
        String token = tokenResp.getToken();
        Assert.assertNotNull(token);

        // 3. Appeler endpoint protégé sans token -> 401
        Response unauthorizedResponse = target("/annonces").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(new AnnonceDTO(), MediaType.APPLICATION_JSON));
        Assert.assertEquals(401, unauthorizedResponse.getStatus());

        // 4. Appeler endpoint protégé avec token -> 200 (ou 400 si validation error,
        // mais pas 401)
        // On teste juste l'accès, donc on s'attend à ce que le SecurityFilter laisse
        // passer
        // On utilise un GET sur /annonces (qui n'est pas @Secured dans le TP par
        // défaut?)
        // Vérifions si le TP demande de sécuriser le POST.

        AnnonceDTO dto = new AnnonceDTO();
        dto.setTitle("Test Title");
        dto.setDescription("Test Desc");
        dto.setAdress("Paris");
        dto.setMail(email);

        Response authorizedResponse = target("/annonces").request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .post(Entity.entity(dto, MediaType.APPLICATION_JSON));

        // Si 201 Created ou 200 OK, c'est que l'auth a réussi
        Assert.assertNotEquals(401, authorizedResponse.getStatus());
        Assert.assertTrue(authorizedResponse.getStatus() == 201 || authorizedResponse.getStatus() == 200);
    }
}
