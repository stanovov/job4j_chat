package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.handlers.Operation;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Message>> findAll() {
        return new ResponseEntity<>(messageService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        return messageService.findById(id)
                .map(message -> ResponseEntity.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(message))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Message is not found by id %d", id)
                ));
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Message> create(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                messageService.saveOrUpdate(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Message message) {
        messageService.saveOrUpdate(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Message> patch(@Valid @RequestBody Message message) {
        return new ResponseEntity<>(
                messageService.patch(message),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        messageService.delete(id);
        return ResponseEntity.ok().build();
    }
}
