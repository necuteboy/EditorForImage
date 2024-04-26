package com.example.editorforimages.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_ref")
public class UserEntity {
    @Id
    @SequenceGenerator(name = "generator", sequenceName = "user_ref_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    private  Long id;
    @Column(name = "name")
    private  String name;

    @Column(name = "password")
    private String password;
    @Transient
    private String passwordConfirm;

    @Column(name = "role")
    private String role;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;
}
