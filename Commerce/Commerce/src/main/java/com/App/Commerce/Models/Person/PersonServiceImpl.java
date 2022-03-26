package com.App.Commerce.Models.Person;

import com.App.Commerce.Exceptions.ApiNotFoundException;
import com.App.Commerce.Exceptions.ApiRequestException;
import com.App.Commerce.Models.Address.AddressEntity;
import com.App.Commerce.Models.Address.AddressService;
import com.App.Commerce.Models.AppUser.AppUserEntity;
import com.App.Commerce.Models.AppUser.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService{
    private PersonRepository personRepository;
    private AddressService addressService;

    @Override
    public List<PersonEntity> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Long addPerson(PersonEntity person) {

        validatePersonDetails(person);

        addressService.validateAddressDetails(person.getAddressEntity());

        return personRepository.save(person).getId();
    }

    @Override
    public PersonEntity update(Long id, PersonEntity updatePerson) {


        PersonEntity personToUpdate = personRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException("User to update does not exists"));

        validatePersonDetails(updatePerson);

        personToUpdate.setFirstname(updatePerson.getFirstname());
        personToUpdate.setLastname(updatePerson.getLastname());
        personToUpdate.setBirthDate(updatePerson.getBirthDate());
        personToUpdate.setSex(updatePerson.getSex());


        AddressEntity personAddress = Optional.ofNullable(updatePerson.getAddressEntity())
                .orElseThrow(() -> new ApiNotFoundException("Adress details can not be empty."));

        addressService.validateAddressDetails(personAddress);

        personToUpdate.setAddressEntity(personAddress);


        return personRepository.save(personToUpdate);
    }

    @Override
    public PersonEntity getPerson(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(String.format("Person %s was not found.", id)));
    }

    @Override
    public void validatePersonDetails(PersonEntity person) {

        Optional.ofNullable(person)
                .orElseThrow(() -> new ApiRequestException("Person can not be empty."));

        Optional.ofNullable(person.getSex())
                .orElseThrow(() -> new ApiRequestException("Sex can not be null."));

        Optional.ofNullable(person.getFirstname())
                .orElseThrow(() -> new ApiRequestException("Firstname  can not be null."));

        Optional.ofNullable(person.getLastname())
                .orElseThrow(() -> new ApiRequestException("Lastname can not be null."));

        Optional.ofNullable(person.getBirthDate())
                .orElseThrow(() -> new ApiRequestException("Birthdate can not be null."));
    }

    @Override
    public void deleteById(Long id) {
        if (personRepository.existsById(id))
            personRepository.deleteById(id);
        else
            throw new ApiNotFoundException(String.format("Person by id: %s does not exists", id));
    }

    @Override
    public Period getAge(final Long id) {
        PersonEntity person = personRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(String.format("Person %s was not found.", id)));

        return Period.between(person.getBirthDate(), LocalDate.now());
    }
}
