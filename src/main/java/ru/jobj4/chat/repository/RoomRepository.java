package ru.jobj4.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.chat.model.Room;

public interface RoomRepository extends CrudRepository<Room, Integer> {
}
