package com.bar.behdavardatabase.entity;

import com.bar.behdavardatabase.common.BaseAuditorEntity;
import com.bar.behdavardatabase.constant.ContactConstant;
import com.bar.behdavardatabase.constant.common.BaseConstant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.bar.behdavardatabase.constant.common.BaseConstant.BASE_TABLE_PREFIX;

@Setter
@Getter
@Entity
@Table(name = ContractEntity.TABLE_NAME, schema = ContactConstant.SCHEMA)
public class ContractEntity extends BaseAuditorEntity<String, Long> {

    public static final String TABLE_NAME = BASE_TABLE_PREFIX + "CONTRACT";
    public static final String SEQ_NAME = "CONTRACT" + BaseConstant.SEQUENCE;

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ContractEntity.SEQ_NAME)
    @SequenceGenerator(name = ContractEntity.SEQ_NAME, sequenceName = ContractEntity.SEQ_NAME)
    private Long id;

}