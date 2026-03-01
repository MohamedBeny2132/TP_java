package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao.AnnonceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnonceServiceTest {

    @Mock
    private AnnonceRepository annonceRepository;

    @InjectMocks
    private AnnonceService annonceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAnnonceById_shouldReturnAnnonce() {
        Annonce annonce = new Annonce();
        annonce.setId(1);
        annonce.setTitle("Titre test");
        when(annonceRepository.findById(1)).thenReturn(Optional.of(annonce));

        Annonce result = annonceService.getAnnonceById(1);

        assertNotNull(result);
        assertEquals("Titre test", result.getTitle());
        verify(annonceRepository, times(1)).findById(1);
    }

    @Test
    void getAnnonceById_whenNotFound_shouldReturnNull() {
        when(annonceRepository.findById(99)).thenReturn(Optional.empty());

        Annonce result = annonceService.getAnnonceById(99);

        assertNull(result);
        verify(annonceRepository, times(1)).findById(99);
    }

    @Test
    void createAnnonce_shouldSave() {
        Annonce annonce = new Annonce();
        annonce.setTitle("A vendre");

        annonceService.createAnnonce(annonce);

        verify(annonceRepository, times(1)).save(annonce);
    }
}
