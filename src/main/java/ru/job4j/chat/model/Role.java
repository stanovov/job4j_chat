package ru.job4j.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id must be more than 0", groups = Operation.OnUpdate.class)
    private int id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Name must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String name;

    @OneToMany(mappedBy = "role")
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private Set<Person> persons = new HashSet<>();

    public static Role of(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
