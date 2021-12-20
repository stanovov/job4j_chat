package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleService {

    private final RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> findAll() {
        return StreamSupport.stream(
                roleRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Role> findById(int id) {
        return roleRepo.findById(id);
    }

    public Role saveOrUpdate(Role role) {
        return roleRepo.save(role);
    }

    public Role patch(Role role) {
        if (!roleRepo.existsById(role.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        roleRepo.save(role);
        return role;
    }

    public void delete(int id) {
        Role role = new Role();
        role.setId(id);
        roleRepo.delete(role);
    }

}
