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
@Table(name = MessageEntity.TABLE_NAME, schema = ContactConstant.SCHEMA)
public class MessageEntity extends BaseAuditorEntity<String, Long> {

    public static final String TABLE_NAME = BASE_TABLE_PREFIX + "MESSAGE";
    public static final String SEQ_NAME = "MESSAGE" + BaseConstant.SEQUENCE;

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MessageEntity.SEQ_NAME)
    @SequenceGenerator(name = MessageEntity.SEQ_NAME, sequenceName = MessageEntity.SEQ_NAME)
    private Long id;

}