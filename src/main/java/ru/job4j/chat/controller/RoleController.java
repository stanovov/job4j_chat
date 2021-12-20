package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.RoleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/")
    public List<Role> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable int id) {
        return roleService.findById(id)
                .map(role -> new ResponseEntity<>(role, HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Role is not found by id %d", id)
                ));
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        return new ResponseEntity<>(
                roleService.saveOrUpdate(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Role role) {
        roleService.saveOrUpdate(role);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Role> patch(@Valid @RequestBody Role role) {
        return new ResponseEntity<>(
                roleService.patch(role),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Valid @PathVariable int id) {
        roleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
