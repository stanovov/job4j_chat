package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.PersonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getSimpleName());

    private final PersonService personService;

    private final ObjectMapper objectMapper;

    public PersonController(PersonService personService, ObjectMapper objectMapper) {
        this.personService = personService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        return personService.findById(id)
                .map(person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    @GetMapping("/{id}/role/")
    public ResponseEntity<Role> findPersonRoleById(@PathVariable int id) {
        return personService.findPersonRoleById(id)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    @GetMapping("/{id}/rooms/")
    public ResponseEntity<Set<Room>> findPersonRoomsById(@PathVariable int id) {
        return personService.findPersonRoomsById(id)
                .map(rooms -> new ResponseEntity<>(rooms, HttpStatus.OK))
                .orElseThrow(() -> generateExceptionPersonNotFoundById(id));
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public void signUp(@Valid @RequestBody Person person) {
        personService.saveOrUpdate(person);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        personService.saveOrUpdate(person);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Person> patch(@Valid @RequestBody Person person) {
        return new ResponseEntity<>(
                personService.patch(person),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}/role/")
    public ResponseEntity<Void> updateRole(@PathVariable int id, @RequestBody Role role) {
        try {
            personService.updatePersonRole(id, role);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/rooms/")
    public ResponseEntity<Void> addRoom(@PathVariable int id, @RequestBody Room room) {
        try {
            personService.addPersonRoom(id, room);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        personService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/role/")
    public ResponseEntity<Void> deleteRole(@PathVariable int id) {
        try {
            personService.deletePersonRole(id);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id, @PathVariable int roomId) {
        try {
            personService.deletePersonRoom(id, roomId);
        } catch (IllegalArgumentException e) {
            throw generateExceptionPersonNotFoundById(id);
        }
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

    private ResponseStatusException generateExceptionPersonNotFoundById(int id) {
        return new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Person is not found by id %d", id)
        );
    }
}
