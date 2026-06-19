package com.example.simplejavawebapp.service;

import com.example.simplejavawebapp.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {

    private final ConcurrentHashMap<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<Todo> findAll() {
        return new ArrayList<>(todos.values())
                .stream()
                .sorted(Comparator.comparing(Todo::getId))
                .toList();
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    public Todo create(String title, String description) {
        Long id = idGenerator.incrementAndGet();
        Todo todo = new Todo(id, title, description, false);
        todos.put(id, todo);
        return todo;
    }

    public Optional<Todo> update(Long id, String title, String description, boolean completed) {
        Todo existingTodo = todos.get(id);

        if (existingTodo == null) {
            return Optional.empty();
        }

        existingTodo.setTitle(title);
        existingTodo.setDescription(description);
        existingTodo.setCompleted(completed);

        return Optional.of(existingTodo);
    }

    public Optional<Todo> toggleCompleted(Long id) {
        Todo existingTodo = todos.get(id);

        if (existingTodo == null) {
            return Optional.empty();
        }

        existingTodo.setCompleted(!existingTodo.isCompleted());

        return Optional.of(existingTodo);
    }

    public void delete(Long id) {
        todos.remove(id);
    }
}