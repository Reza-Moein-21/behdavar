package com.bar.behdavarbackend.business.transformer;

import com.bar.behdavarbackend.dto.CatalogDetailDto;
import com.bar.behdavardatabase.entity.CatalogDetailEntity;

public class CatalogDetailTransformer {

    public static CatalogDetailEntity DTO_TO_ENTITY(CatalogDetailDto dto, CatalogDetailEntity entity) {
        entity.setCode(dto.getCode());
        entity.setEnglishTitle(dto.getEnglishTitle());
        entity.setActive(entity.getActive());
        entity.setTitle(dto.getTitle());
        entity.setCatalog(CatalogTransformer.CREATE_ENTITY_FOR_RELATION(dto.getCatalog().getId()));
        return entity;
    }

    public static CatalogDetailEntity CREATE_ENTITY_FOR_RELATION(Long id) {
        CatalogDetailEntity entity = new CatalogDetailEntity();
        entity.setId(id);
        return entity;
    }
}