package com.App.Commerce.Models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
    boolean  existsByUsername(String username);
    void deleteByUsername(String username);
    //@Query("SELECT s FROM users s WHERE s.email = ?1")
    boolean existByEmail(String username);
}
