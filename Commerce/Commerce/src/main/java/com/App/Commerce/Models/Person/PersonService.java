package com.App.Commerce.Models.Person;

import java.util.List;

public interface PersonService {

    List<PersonEntity> getAll();
    Long addPerson(final PersonEntity person);
    //todo:?
/*    Role saveRole(final Role role);
    void addRoleToPerson(final String username, final String roleName);*/
    PersonEntity update(Long id, final PersonEntity updatePerson);
    PersonEntity getPerson(final Long id);
    void validatePersonDetails(final PersonEntity person);
    void deleteById(final Long id);
}
