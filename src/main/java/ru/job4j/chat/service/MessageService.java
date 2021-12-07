package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MessageService {

    private final MessageRepository messageRepo;

    public MessageService(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    public List<Message> findAll() {
        return StreamSupport.stream(
                messageRepo.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<Message> findById(int id) {
        return messageRepo.findById(id);
    }

    public Message saveOrUpdate(Message message) {
        return messageRepo.save(message);
    }

    public Message patch(Message message) throws InvocationTargetException, IllegalAccessException {
        var current = messageRepo.findById(message.getId())
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
                var newValue = getMethod.invoke(message);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        messageRepo.save(message);
        return current;
    }

    public void delete(int id) {
        Message message = new Message();
        message.setId(id);
        messageRepo.delete(message);
    }
}
