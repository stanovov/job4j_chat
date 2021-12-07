package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
        validate(role);
        return roleRepo.save(role);
    }

    public Role patch(Role role) throws InvocationTargetException, IllegalAccessException {
        var current = roleRepo.findById(role.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var methods = current.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid properties mapping");
                }
                var newValue = getMethod.invoke(role);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        roleRepo.save(role);
        return current;
    }

    public void delete(int id) {
        Role role = new Role();
        role.setId(id);
        roleRepo.delete(role);
    }

    private void validate(Role role) {
        if (role.getName() == null) {
            throw new NullPointerException("Role name mustn't be empty");
        }
    }
}
