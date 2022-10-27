package org.hbrs.project.wram.control.entwickler;

import org.hbrs.project.wram.model.entwickler.user.Entwickler;
import org.hbrs.project.wram.model.entwickler.user.EntwicklerDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntwicklerMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "user", source = "user")
    EntwicklerDTO toDTO(Entwickler entwickler);
}