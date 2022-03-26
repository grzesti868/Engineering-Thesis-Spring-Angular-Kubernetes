package com.App.Commerce.Models.Person;

import java.time.Period;
import java.util.List;

public interface PersonService {

    List<PersonEntity> getAll();
    Long addPerson(final PersonEntity person);
    PersonEntity update(Long id, final PersonEntity updatePerson);
    PersonEntity getPerson(final Long id);
    void validatePersonDetails(final PersonEntity person);
    void deleteById(final Long id);
    Period getAge(final Long id);
}
