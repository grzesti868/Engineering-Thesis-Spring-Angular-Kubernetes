package com.App.Commerce.Models.AppUser;

import com.App.Commerce.Enums.StatusEnum;
import com.App.Commerce.Models.Person.PersonEntity;
import com.App.Commerce.Models.Role.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    AppUserEntity findByUsername(String username);
    boolean  existsByUsername(String username);
    void deleteByUsername(String username);
    //@Query("SELECT s FROM users s WHERE s.email = ?1")
    boolean existsByEmail(String email);

}
