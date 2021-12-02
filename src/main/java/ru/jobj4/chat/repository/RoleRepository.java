package ru.jobj4.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.chat.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
