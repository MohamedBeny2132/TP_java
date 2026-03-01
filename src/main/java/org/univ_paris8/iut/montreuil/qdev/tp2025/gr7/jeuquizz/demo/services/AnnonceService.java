package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.PaginatedResponses;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@Service
public class AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    public void createAnnonce(Annonce annonce) {
        annonceRepository.save(annonce);
    }

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    public PaginatedResponses<Annonce> getAllAnnoncesWithParams(AnnonceSearchParams params) {
        Sort.Direction direction = "asc".equalsIgnoreCase(params.getSortDir()) ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, params.getSortBy() != null ? params.getSortBy() : "date");

        
        Pageable pageable = PageRequest.of(params.getPage() - 1, params.getSize(), sort);

        Specification<Annonce> spec = AnnonceSpecifications.withParams(params);
        Page<Annonce> page = annonceRepository.findAll(spec, pageable);

        return new PaginatedResponses<>(page.getContent(), params.getPage(),
                page.getTotalPages() == 0 ? 1 : page.getTotalPages());
    }

    public Annonce getAnnonceById(int id) {
        Optional<Annonce> ann = annonceRepository.findById(id);
        return ann.orElse(null);
    }

    public void updateAnnonce(Annonce annonce) {
        annonceRepository.save(annonce);
    }

    public void deleteAnnonce(int id) {
        annonceRepository.deleteById(id);
    }
}