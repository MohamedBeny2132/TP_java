package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Status;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.User;

@Service("securityService")
public class SecurityService {

    @Autowired
    private AnnonceService annonceService;

    public boolean isAnnonceAuthor(User principal, int annonceId) {
        if (principal == null)
            return false;
        Annonce annonce = annonceService.getAnnonceById(annonceId);
        if (annonce == null)
            return false;
        return annonce.getAuthor() != null && annonce.getAuthor().getId() == principal.getId();
    }

    public boolean isAnnonceNotPublished(int annonceId) {
        Annonce annonce = annonceService.getAnnonceById(annonceId);
        if (annonce == null)
            return false;
        return annonce.getStatus() != Status.PUBLISHED;
    }
}
