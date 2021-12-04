package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/users")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    private final PersonRepository personRepo;

    private final BCryptPasswordEncoder encoder;

    private final ObjectMapper objectMapper;

    public PersonController(PersonRepository personRepo, BCryptPasswordEncoder encoder, ObjectMapper objectMapper) {
        this.personRepo = personRepo;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return StreamSupport.stream(
                personRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return personRepo.findById(id)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseThrow(generateExceptionPersonNotFoundById(id));
    }

    @GetMapping("/{id}/role/")
    public ResponseEntity<Role> findPersonRoleById(@PathVariable int id) {
        return personRepo.findById(id)
                .map(person -> new ResponseEntity<>(
                        person.getRole() == null ? new Role() : person.getRole(),
                        HttpStatus.OK
                ))
                .orElseThrow(generateExceptionPersonNotFoundById(id));
    }

    @GetMapping("/{id}/rooms/")
    public ResponseEntity<Set<Room>> findPersonRoomsById(@PathVariable int id) {
        return personRepo.findById(id)
                .map(person -> new ResponseEntity<>(
                        person.getRooms(),
                        HttpStatus.OK
                ))
                .orElseThrow(generateExceptionPersonNotFoundById(id));
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        validate(person);
        person.setPassword(encoder.encode(person.getPassword()));
        personRepo.save(person);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        validate(person);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/role/")
    public ResponseEntity<Void> updateRole(@PathVariable int id, @RequestBody Role role) {
        Person person = personRepo.findById(id)
                .orElseThrow(generateExceptionPersonNotFoundById(id));
        if (role.getId() == 0) {
            throw new NullPointerException("Role id mustn't be empty");
        }
        person.setRole(role);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/rooms/")
    public ResponseEntity<Void> addRoom(@PathVariable int id, @RequestBody Room room) {
        Person person = personRepo.findById(id)
                .orElseThrow(generateExceptionPersonNotFoundById(id));
        if (room.getId() == 0) {
            throw new NullPointerException("Room id mustn't be empty");
        }
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
        Person person = personRepo.findById(id)
                .orElseThrow(generateExceptionPersonNotFoundById(id));
        person.setRole(null);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id, @PathVariable int roomId) {
        Person person = personRepo.findById(id)
                .orElseThrow(generateExceptionPersonNotFoundById(id));
        person.getRooms().removeIf(room -> room.getId() == roomId);
        personRepo.save(person);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = { IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
            put("message", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }

    private Supplier<ResponseStatusException> generateExceptionPersonNotFoundById(int id) {
        return () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Person is not fount by id %d", id)
        );
    }

    private void validate(Person person) {
        if (person.getUsername() == null || person.getPassword() == null) {
            throw new NullPointerException("User username and password mustn't be empty");
        }
        validatePassword(person.getPassword());
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Invalid password. Password must be at least 8 characters long.");
        }
        boolean check = false;
        boolean upperCase = false;
        boolean lowerCase = false;
        boolean number = false;
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                upperCase = true;
            }
            if (Character.isLowerCase(ch)) {
                lowerCase = true;
            }
            if (Character.isDigit(ch)) {
                number = true;
            }
            check = upperCase && lowerCase && number;
            if (check) {
                break;
            }
        }
        if (!check) {
            throw new IllegalArgumentException(
                    "Invalid password. The password must contain at least three character categories"
                            + " among the following: uppercase characters, lowercase characters and digits"
            );
        }
    }
}
