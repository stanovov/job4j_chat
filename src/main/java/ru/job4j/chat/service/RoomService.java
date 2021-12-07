package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoomService {

    private final RoomRepository roomRepo;

    public RoomService(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public List<Room> findAll() {
        return StreamSupport.stream(
                roomRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Room> findById(int id) {
        return roomRepo.findById(id);
    }

    public Room saveOrUpdate(Room room) {
        validate(room);
        return roomRepo.save(room);
    }

    public Room patch(Room room) throws InvocationTargetException, IllegalAccessException {
        var current = roomRepo.findById(room.getId())
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
                var newValue = getMethod.invoke(room);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        roomRepo.save(room);
        return current;
    }

    public void delete(int id) {
        Room room = new Room();
        room.setId(id);
        roomRepo.delete(room);
    }

    private void validate(Room room) {
        if (room.getName() == null) {
            throw new NullPointerException("Room name mustn't be empty");
        }
    }
}
