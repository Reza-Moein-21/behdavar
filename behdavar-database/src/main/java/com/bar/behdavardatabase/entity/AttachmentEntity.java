package com.bar.behdavardatabase.entity;

import com.bar.behdavardatabase.common.BaseAuditorEntity;
import com.bar.behdavardatabase.constant.ContactConstant;
import com.bar.behdavardatabase.constant.common.BaseConstant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.bar.behdavardatabase.constant.common.BaseConstant.BASE_TABLE_PREFIX;

@Setter
@Getter
@Audited
@AuditOverrides({@AuditOverride(forClass = BaseAuditorEntity.class)})
@Entity
@Table(name = AttachmentEntity.TABLE_NAME, schema = ContactConstant.SCHEMA)
public class AttachmentEntity extends BaseAuditorEntity<String, Long> {

    public static final String TABLE_NAME = BASE_TABLE_PREFIX + "ATTACHMENT";
    public static final String SEQ_NAME = "ATTACHMENT" + BaseConstant.SEQUENCE;
    public static final String CONTENT = "content";
    public static final String FILENAME = "fileName";
    public static final String ATTACHMENT_TYPE = "attachmentType";
    public static final String CONTRACT = "contract";

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = AttachmentEntity.SEQ_NAME)
    @SequenceGenerator(name = AttachmentEntity.SEQ_NAME, sequenceName = AttachmentEntity.SEQ_NAME, allocationSize = ALLOCATION_SIZE)
    private Long id;

    @Lob
    @Column(name = "CONTENT",nullable = false)
    private byte[] content;

    @Column(name = "FILE_NAME", nullable = false)
    @NotNull
    private String fileName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTRACT_ID", foreignKey = @ForeignKey(name = "ATTACHMENT_CONTRACT_FK"))
    private ContractEntity contract;


}