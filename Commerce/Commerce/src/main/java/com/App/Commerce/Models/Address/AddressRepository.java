package com.App.Commerce.Models.Address;

import com.App.Commerce.Models.Person.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
