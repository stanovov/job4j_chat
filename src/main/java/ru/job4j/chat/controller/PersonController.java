package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class PersonController {

    private final PersonRepository personRepo;

    private final BCryptPasswordEncoder encoder;

    public PersonController(PersonRepository personRepo, BCryptPasswordEncoder encoder) {
        this.personRepo = personRepo;
        this.encoder = encoder;
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

    @GetMapping("/{id}/role/")
    public ResponseEntity<Role> findPersonRoleById(@PathVariable int id) {
        var person = personRepo.findById(id);
        Role role = null;
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (person.isPresent()) {
            status = HttpStatus.OK;
            role = person.get().getRole();
        }
        return new ResponseEntity<>(
                role == null ? new Role() : role,
                status
        );
    }

    @GetMapping("/{id}/rooms/")
    public ResponseEntity<Set<Room>> findPersonRoomsById(@PathVariable int id) {
        var person = personRepo.findById(id);
        Set<Room> rooms = Set.of();
        HttpStatus status = HttpStatus.NOT_FOUND;
        if (person.isPresent()) {
            status = HttpStatus.OK;
            rooms = person.get().getRooms();
        }
        return new ResponseEntity<>(rooms, status);
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        personRepo.save(person);
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

    @PutMapping("/{id}/rooms/")
    public ResponseEntity<Void> addRoom(@PathVariable int id, @RequestBody Room room) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.addRoom(room);
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

    @DeleteMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id, @PathVariable int roomId) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Person person = optionalPerson.get();
        person.getRooms().removeIf(room -> room.getId() == roomId);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }
}
