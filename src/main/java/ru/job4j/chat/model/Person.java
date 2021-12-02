package ru.job4j.chat.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "persons")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinTable(
            name = "persons_in_rooms",
            joinColumns = {@JoinColumn(name = "person_id", nullable = false, unique = true)},
            inverseJoinColumns = {@JoinColumn(name = "room_id", nullable = false)}
    )
    private Room room;

    @ManyToOne
    @JoinTable(
            name = "role_of_persons",
            joinColumns = {@JoinColumn(name = "person_id", nullable = false, unique = true)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false)}
    )
    private Role role;

    public static Person of(String username, String password,
                            Room room, Role role) {
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        person.setRoom(room);
        person.setRole(role);
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
