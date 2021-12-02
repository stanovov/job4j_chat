package ru.jobj4.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.chat.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
