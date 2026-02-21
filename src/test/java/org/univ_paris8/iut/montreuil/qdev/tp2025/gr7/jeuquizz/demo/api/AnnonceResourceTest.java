package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.mapper.GlobalExceptionMapper;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.mapper.ValidationExceptionMapper;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AnnonceResourceTest extends JerseyTest {

    private AnnonceService mockService;

    @Override
    protected Application configure() {
        mockService = Mockito.mock(AnnonceService.class);
        AnnonceResource resource = new AnnonceResource();
        resource.setAnnonceService(mockService);

        ResourceConfig config = new ResourceConfig();
        config.register(resource);
        config.register(JacksonFeature.class);
        config.register(GlobalExceptionMapper.class);
        config.register(ValidationExceptionMapper.class);

        return config;
    }

    @Test
    public void testGetAnnoncesEmpty() {
        PaginatedResponses<Annonce> emptyPage = new PaginatedResponses<>(new ArrayList<>(), 1, 0);
        when(mockService.getAllAnnoncesWithParams(any(AnnonceSearchParams.class))).thenReturn(emptyPage);

        Response response = target("/annonces").request().get();

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PaginatedResponses<?> result = response.readEntity(PaginatedResponses.class);
        Assert.assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void testGetAnnonceNotFound() {
        when(mockService.getAnnonceById(99)).thenReturn(null);

        Response response = target("/annonces/99").request().get();

        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateAnnonceValidationError() {
        // Envoi d'un DTO vide (le titre est obligatoire)
        AnnonceDTO invalidDto = new AnnonceDTO();

        Response response = target("/annonces").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(invalidDto, MediaType.APPLICATION_JSON));

        // @Valid devrait déclencher une erreur 400 via ValidationExceptionMapper
        if (response.getStatus() != 400) {
            System.out.println("DEBUG: Response Status = " + response.getStatus());
            System.out.println("DEBUG: Response Body = " + response.readEntity(String.class));
        }
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
