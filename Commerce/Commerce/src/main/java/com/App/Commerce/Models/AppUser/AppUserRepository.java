package com.App.Commerce.Models.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByUsername(String username);
    boolean  existsByUsername(String username);
    void deleteByUsername(String username);
    //@Query("SELECT s FROM users s WHERE s.email = ?1")
    boolean existsByEmail(String email);

}
