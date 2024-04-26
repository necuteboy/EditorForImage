package com.example.editorforimages.repository;

import com.example.editorforimages.entity.Role;
import com.example.editorforimages.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    List<UserEntity> findByRole(Role role);
    UserEntity findByName(String name);

}
