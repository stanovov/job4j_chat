package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepo;

    public PersonController(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return StreamSupport.stream(
                personRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personRepo.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<>(
                personRepo.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/role/")
    public ResponseEntity<Void> updateRole(@PathVariable int id, @RequestBody Role role) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.setRole(role);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/room/")
    public ResponseEntity<Void> updateRoom(@PathVariable int id, @RequestBody Room room) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.setRoom(room);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Person person = new Person();
        person.setId(id);
        personRepo.delete(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/role/")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.setRole(null);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/room/")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.setRoom(null);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }
}
