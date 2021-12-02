package ru.jobj4.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jobj4.chat.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
}
