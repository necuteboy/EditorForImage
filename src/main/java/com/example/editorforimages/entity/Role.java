package com.example.editorforimages.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles_jn")
public class Role {
    @Id
    @SequenceGenerator(name = "generator", sequenceName = "roles_jn_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    private Integer id;

    @Column(name = "name")
    private String name;
}
