package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class JaxRsApplication extends ResourceConfig {

    public JaxRsApplication() {
        // Définition de la configuration JAAS
        java.net.URL jaasConfig = getClass().getClassLoader().getResource("jaas.conf");
        if (jaasConfig != null) {
            System.setProperty("java.security.auth.login.config", jaasConfig.toExternalForm());
        }

        // Scan automatique du package "api" pour trouver toutes les @Path et @Provider
        packages("org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api");

        // Active la sérialisation JSON via Jackson
        register(JacksonFeature.class);
    }
}
