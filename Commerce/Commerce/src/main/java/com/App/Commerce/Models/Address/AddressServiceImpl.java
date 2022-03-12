package com.App.Commerce.Models.Address;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Person.PersonEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService{
    private AddressRepository addressRepository;

    @Override
    public List<AddressEntity> getAll() {
        return addressRepository.findAll();
    }

    @Override
    public Long addAddress(AddressEntity address) {
        AddressEntity newAddress = Optional.ofNullable(address)
                .orElseThrow(() -> new ApiRequestException("New address can not be empty."));

        validateAddressDetails(newAddress);

        return addressRepository.save(newAddress).getId();
    }

    @Override
    public AddressEntity update(Long id, AddressEntity address) {
        AddressEntity updateAddress = Optional.ofNullable(address)
                .orElseThrow(() -> new ApiRequestException("Address can not be empty."));

        AddressEntity addressToUpdate = addressRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("Address to update does not exists"));

        validateAddressDetails(updateAddress);

        addressToUpdate.setCity(updateAddress.getCity());
        addressToUpdate.setApartment_num(updateAddress.getApartment_num());
        addressToUpdate.setBuilding_num(updateAddress.getBuilding_num());
        addressToUpdate.setCountry(updateAddress.getCountry());
        addressToUpdate.setPostal_code(updateAddress.getPostal_code());
        addressToUpdate.setStreet(updateAddress.getStreet());

        return addressRepository.save(addressToUpdate);
    }

    @Override
    public AddressEntity getAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(String.format("Address %s was not found.", id)));
    }

    @Override
    public void validateAddressDetails(AddressEntity address) {
        Optional.ofNullable(address.getCountry())
                .orElseThrow(() -> new ApiRequestException("Country can not be null."));

        Optional.ofNullable(address.getPostal_code())
                .orElseThrow(() -> new ApiRequestException("Postal  can not be null."));

        Optional.ofNullable(address.getStreet())
                .orElseThrow(() -> new ApiRequestException("Street  can not be null."));

        Optional.ofNullable(address.getBuilding_num())
                .orElseThrow(() -> new ApiRequestException("Building number can not be null."));

        Optional.ofNullable(address.getCity())
                .orElseThrow(() -> new ApiRequestException("City  can not be null."));
    }

    @Override
    public void deleteById(Long id) {
        if (addressRepository.existsById(id))
            addressRepository.deleteById(id);
        else
            throw new ApiNotFoundException(String.format("Address by id: %s does not exists", id));
    }
}
