package com.bar.behdavarbackend.business.transformer;

import com.bar.behdavarbackend.dto.CatalogDto;
import com.bar.behdavardatabase.entity.CatalogEntity;

public class CatalogTransformer extends BaseAuditorTransformer {

    public static CatalogEntity dtoToEntity(CatalogDto dto, CatalogEntity entity) {
        entity.setCode(dto.getCode());
        entity.setEnglishTitle(dto.getEnglishTitle());
        entity.setActive(dto.getActive());
        entity.setTitle(dto.getTitle());
        return entity;
    }

    public static CatalogDto entityToDto(CatalogEntity entity, CatalogDto dto) {
        transformAuditingFields(entity, dto);
        dto.setCode(entity.getCode());
        dto.setEnglishTitle(entity.getEnglishTitle());
        dto.setActive(entity.getActive());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public static CatalogEntity createEntityForRelation(Long id) {
        CatalogEntity entity = new CatalogEntity();
        entity.setId(id);
        return entity;
    }
}
