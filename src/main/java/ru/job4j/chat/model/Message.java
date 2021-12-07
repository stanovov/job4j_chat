package ru.job4j.chat.model;

import org.hibernate.annotations.CreationTimestamp;
import ru.job4j.chat.handlers.Operation;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id must be more than 0", groups = Operation.OnUpdate.class)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Calendar created;

    @Column(nullable = false)
    @NotBlank(message = "Text must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private String text;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    @NotNull(message = "Room must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private Room room;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @NotNull(message = "Person must be not empty", groups = {
            Operation.OnCreate.class, Operation.OnUpdate.class
    })
    private Person person;

    public static Message of(String text, Room room, Person person) {
        Message message = new Message();
        message.setText(text);
        message.setRoom(room);
        message.setPerson(person);
        message.setCreated(Calendar.getInstance());
        return message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
