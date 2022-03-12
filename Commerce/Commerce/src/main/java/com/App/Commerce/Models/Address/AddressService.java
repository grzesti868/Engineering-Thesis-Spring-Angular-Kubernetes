package com.App.Commerce.Models.Address;

import com.App.Commerce.Models.Person.PersonEntity;

import java.util.List;

public interface AddressService {

    List<AddressEntity> getAll();
    Long addAddress(final AddressEntity address);
    AddressEntity update(Long id, final AddressEntity address);
    AddressEntity getAddress(final Long id);
    void validateAddressDetails(final AddressEntity address);
    void deleteById(final Long id);
}
