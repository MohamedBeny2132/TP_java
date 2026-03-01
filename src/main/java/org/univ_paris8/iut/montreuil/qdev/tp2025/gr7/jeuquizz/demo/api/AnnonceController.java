package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.mapper.AnnonceMapper;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services.AnnonceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private static final Logger logger = LoggerFactory.getLogger(AnnonceController.class);

    private final AnnonceService annonceService;
    private final AnnonceMapper annonceMapper;

    public AnnonceController(AnnonceService annonceService, AnnonceMapper annonceMapper) {
        this.annonceService = annonceService;
        this.annonceMapper = annonceMapper;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponses<AnnonceDTO>> getAllAnnonces(AnnonceSearchParams params) {
        logger.info("Requête GET /api/annonces");
        PaginatedResponses<Annonce> page = annonceService.getAllAnnoncesWithParams(params);

        List<AnnonceDTO> dtos = page.getItems().stream()
                .map(annonceMapper::toDto)
                .collect(Collectors.toList());

        PaginatedResponses<AnnonceDTO> response = new PaginatedResponses<>(
                dtos, page.getCurrentPage(), page.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable("id") int id) {
        Annonce annonce = annonceService.getAnnonceById(id);
        if (annonce == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(annonceMapper.toDto(annonce));
    }

    @PostMapping
    public ResponseEntity<AnnonceDTO> createAnnonce(@Valid @RequestBody AnnonceDTO annonceDTO) {

        Annonce annonce = annonceMapper.toEntity(annonceDTO);
        annonceService.createAnnonce(annonce);
        return ResponseEntity.status(HttpStatus.CREATED).body(annonceMapper.toDto(annonce));
    }

    @PreAuthorize("@securityService.isAnnonceAuthor(principal, #id) and @securityService.isAnnonceNotPublished(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnnonce(@PathVariable("id") int id, @Valid @RequestBody AnnonceDTO annonceDTO) {
        Annonce existing = annonceService.getAnnonceById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        annonceMapper.updateEntityFromDto(annonceDTO, existing);
        annonceService.updateAnnonce(existing);
        return ResponseEntity.ok(annonceMapper.toDto(existing));
    }

    @PreAuthorize("@securityService.isAnnonceAuthor(principal, #id) and @securityService.isAnnonceNotPublished(#id)")
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAnnonce(@PathVariable("id") int id, @RequestBody AnnonceDTO annonceDTO) {
        Annonce existing = annonceService.getAnnonceById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        annonceMapper.updateEntityFromDto(annonceDTO, existing);
        annonceService.updateAnnonce(existing);
        return ResponseEntity.ok(annonceMapper.toDto(existing));
    }

    @PreAuthorize("@securityService.isAnnonceAuthor(principal, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable("id") int id) {
        Annonce existing = annonceService.getAnnonceById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        annonceService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }
}
