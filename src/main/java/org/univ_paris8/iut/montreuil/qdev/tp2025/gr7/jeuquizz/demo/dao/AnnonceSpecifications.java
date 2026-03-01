package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.AnnonceSearchParams;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class AnnonceSpecifications {

    public static Specification<Annonce> withParams(AnnonceSearchParams params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(params.getKeyword())) {
                String keyword = "%" + params.getKeyword().toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keyword);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), keyword);
                predicates.add(criteriaBuilder.or(titlePredicate, descPredicate));
            }

            if (params.getCategoryId() != null && params.getCategoryId() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), params.getCategoryId()));
            }

            if (params.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), params.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
