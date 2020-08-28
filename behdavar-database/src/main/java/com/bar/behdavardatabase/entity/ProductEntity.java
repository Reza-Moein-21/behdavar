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
@Table(name = ProductEntity.TABLE_NAME, schema = ContactConstant.SCHEMA)
public class ProductEntity extends BaseAuditorEntity<String, Long> {

    public static final String TABLE_NAME = BASE_TABLE_PREFIX + "PRODUCT";
    public static final String SEQ_NAME = "PRODUCT" + BaseConstant.SEQUENCE;

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ProductEntity.SEQ_NAME)
    @SequenceGenerator(name = ProductEntity.SEQ_NAME, sequenceName = ProductEntity.SEQ_NAME, allocationSize = ALLOCATION_SIZE)
    private Long id;

}