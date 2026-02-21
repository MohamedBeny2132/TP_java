package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.filter.Secured;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/annonces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Annonces", description = "Gestion des annonces")
public class AnnonceResource {

    private static final Logger logger = LoggerFactory.getLogger(AnnonceResource.class);

    private AnnonceService annonceService;

    public AnnonceResource() {
        this.annonceService = new AnnonceService();
    }

    // Utilisé pour les tests (Injection pour Mockito)
    public void setAnnonceService(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @GET
    @Operation(summary = "Liste des annonces", description = "Récupère toutes les annonces avec pagination et filtres")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    public Response getAllAnnonces(@BeanParam AnnonceSearchParams params) {
        logger.info("Requête GET /annonces avec filtres : {}", params);
        PaginatedResponses<Annonce> page = annonceService.getAllAnnoncesWithParams(params);

        List<AnnonceDTO> dtos = page.getItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        PaginatedResponses<AnnonceDTO> response = new PaginatedResponses<>(
                dtos, page.getCurrentPage(), page.getTotalPages());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Détail d'une annonce", description = "Récupère une annonce par son ID")
    @ApiResponse(responseCode = "200", description = "Annonce trouvée")
    @ApiResponse(responseCode = "404", description = "Annonce non trouvée")
    public Response getAnnonceById(@PathParam("id") int id) {
        logger.debug("Requête GET /annonces/{}", id);
        Annonce annonce = annonceService.getAnnonceById(id);
        if (annonce == null) {
            logger.warn("Annonce {} non trouvée", id);
            throw new NotFoundException("Annonce non trouvée avec l'ID : " + id);
        }
        return Response.ok(convertToDTO(annonce)).build();
    }

    @POST
    @Secured
    @Operation(summary = "Créer une annonce", description = "Crée une nouvelle annonce (nécessite authentification)")
    @ApiResponse(responseCode = "201", description = "Annonce créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "401", description = "Non authentifié")
    public Response createAnnonce(@Valid AnnonceDTO annonceDTO,
            @Context javax.ws.rs.core.SecurityContext securityContext,
            @Context javax.ws.rs.container.ContainerRequestContext requestContext) {
        logger.info("Essai de création d'une nouvelle annonce par {}", securityContext.getUserPrincipal().getName());
        org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User author = (org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User) requestContext
                .getProperty("user");

        Annonce annonce = convertToEntity(annonceDTO);
        annonce.setAuthor(author);

        annonceService.createAnnonce(annonce);
        logger.info("Annonce créée avec succès, ID : {}", annonce.getId());
        annonceDTO.setId(annonce.getId());
        return Response.status(Response.Status.CREATED).entity(annonceDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    @Operation(summary = "Modifier une annonce", description = "Met à jour une annonce existante (auteur seulement, si non publiée)")
    @ApiResponse(responseCode = "200", description = "Mise à jour réussie")
    @ApiResponse(responseCode = "403", description = "Action non autorisée")
    @ApiResponse(responseCode = "409", description = "Conflit (annonce déjà publiée)")
    public Response updateAnnonce(@PathParam("id") int id, @Valid AnnonceDTO annonceDTO,
            @Context javax.ws.rs.core.SecurityContext securityContext,
            @Context javax.ws.rs.container.ContainerRequestContext requestContext) {
        logger.info("Mise à jour annonce {} par {}", id, securityContext.getUserPrincipal().getName());
        Annonce existing = annonceService.getAnnonceById(id);
        if (existing == null)
            throw new NotFoundException("Annonce non trouvée");

        org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User currentUser = (org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User) requestContext
                .getProperty("user");

        // Règle 7.1 : Seul l'auteur peut modifier
        if (existing.getAuthor().getId() != currentUser.getId()) {
            logger.warn("Utilisateur {} a tenté de modifier l'annonce {} de {}", currentUser.getId(), id,
                    existing.getAuthor().getId());
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "Vous n'êtes pas l'auteur de cette annonce"))
                    .build();
        }

        // Règle 7.2 : Une annonce PUBLISHED ne peut plus être modifiée
        if (existing.getStatus() == org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Status.PUBLISHED) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Une annonce publiée ne peut plus être modifiée"))
                    .build();
        }

        existing.setTitle(annonceDTO.getTitle());
        existing.setDescription(annonceDTO.getDescription());
        existing.setAdress(annonceDTO.getAdress());
        existing.setMail(annonceDTO.getMail());

        annonceService.updateAnnonce(existing);
        return Response.ok(convertToDTO(existing)).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    @Operation(summary = "Supprimer une annonce", description = "Supprime une annonce (auteur seulement, doit être archivée)")
    @ApiResponse(responseCode = "240", description = "Suppression réussie")
    public Response deleteAnnonce(@PathParam("id") int id,
            @Context javax.ws.rs.core.SecurityContext securityContext,
            @Context javax.ws.rs.container.ContainerRequestContext requestContext) {
        logger.info("Suppression annonce {} par {}", id, securityContext.getUserPrincipal().getName());
        Annonce existing = annonceService.getAnnonceById(id);
        if (existing == null)
            // Error managed by GlobalExceptionMapper
            throw new NotFoundException("Annonce non trouvée");

        org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User currentUser = (org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User) requestContext
                .getProperty("user");

        if (existing.getAuthor().getId() != currentUser.getId()) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Map.of("error", "Vous n'êtes pas l'auteur de cette annonce"))
                    .build();
        }

        // Règle 7.3 : Archivage obligatoire avant suppression
        if (existing.getStatus() != org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Status.ARCHIVED) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "L'annonce doit être archivée avant d'être supprimée"))
                    .build();
        }

        annonceService.deleteAnnonce(id);
        return Response.noContent().build();
    }

    private AnnonceDTO convertToDTO(Annonce a) {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setDescription(a.getDescription());
        dto.setAdress(a.getAdress());
        dto.setMail(a.getMail());
        if (a.getAuthor() != null)
            dto.setAuthorId(a.getAuthor().getId());
        if (a.getCategory() != null)
            dto.setCategoryId(a.getCategory().getId());
        return dto;
    }

    private Annonce convertToEntity(AnnonceDTO dto) {
        Annonce a = new Annonce();
        a.setTitle(dto.getTitle());
        a.setDescription(dto.getDescription());
        a.setAdress(dto.getAdress());
        a.setMail(dto.getMail());
        // Note: Author and Category would ideally be set from IDs via services
        return a;
    }
}
