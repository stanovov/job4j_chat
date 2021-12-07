package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
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
        validate(message);
        return messageRepo.save(message);
    }

    public void delete(int id) {
        Message message = new Message();
        message.setId(id);
        messageRepo.delete(message);
    }

    private void validate(Message message) {
        if (message.getText() == null || message.getPerson() == null || message.getRoom() == null) {
            throw new NullPointerException("Message text, person and room mustn't be empty");
        }
    }
}
