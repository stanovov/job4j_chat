package ru.job4j.chat.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private PersonRepository personRepo;

    public UserDetailsServiceImpl(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.findByUsername(username);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(person.getUsername(), person.getPassword(), Collections.emptyList());
    }
}
