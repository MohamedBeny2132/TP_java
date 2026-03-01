package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.api.dto.AnnonceDTO;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean.Annonce;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AnnonceMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    AnnonceDTO toDto(Annonce annonce);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "version", ignore = true)
    Annonce toEntity(AnnonceDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(AnnonceDTO dto, @MappingTarget Annonce annonce);
}
