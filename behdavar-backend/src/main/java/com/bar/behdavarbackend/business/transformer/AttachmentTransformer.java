package com.bar.behdavarbackend.business.transformer;

import com.bar.behdavarbackend.dto.AttachmentDto;
import com.bar.behdavarbackend.dto.ContractDto;
import com.bar.behdavardatabase.entity.AttachmentEntity;
import org.springframework.util.Base64Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AttachmentTransformer extends BaseAuditorTransformer {

    public static AttachmentEntity DTO_TO_ENTITY(AttachmentDto dto, AttachmentEntity entity) {
        entity.setAttachmentType(CatalogDetailTransformer.CREATE_ENTITY_FOR_RELATION(dto.getAttachmentType().getId()));
        entity.setFileName(dto.getFileName());
        entity.setContent(Base64Utils.decodeFromString(dto.getContent()));
        Optional.ofNullable(dto.getContract()).ifPresent(contractDto ->
                entity.setContract(ContractTransformer.CREATE_ENTITY_FOR_RELATION(contractDto.getId())));


        return entity;
    }

    public static AttachmentDto ENTITY_TO_DTO(AttachmentEntity entity, AttachmentDto dto, String... strings) {
        List<String> fields = Arrays.stream(strings).collect(Collectors.toList());
        transformAuditingFields(entity, dto);
        dto.setFileName(entity.getFileName());
        dto.setContent(Base64Utils.encodeToString(entity.getContent()));
        if (fields.contains(AttachmentEntity.CONTRACT)) {
            dto.setContract(ContractTransformer.ENTITY_TO_DTO(entity.getContract(), new ContractDto()));
        } else {
            dto.setContract(ContractTransformer.CREATE_DTO_FOR_RELATION(entity.getContract().getId()));
        }

        return dto;
    }

    public static AttachmentEntity CREATE_ENTITY_FOR_RELATION(Long id) {
        AttachmentEntity entity = new AttachmentEntity();
        entity.setId(id);
        entity.setVersion(0L);
        return entity;
    }

    public static AttachmentDto CREATE_DTO_FOR_RELATION(Long id) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(id);
        dto.setVersion(0L);
        return dto;
    }
}
