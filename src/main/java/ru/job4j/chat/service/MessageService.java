package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.repository.MessageRepository;

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

    public Message patch(Message message) {
        if (!messageRepo.existsById(message.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        messageRepo.save(message);
        return message;
    }

    public void delete(int id) {
        Message message = new Message();
        message.setId(id);
        messageRepo.delete(message);
    }
}
