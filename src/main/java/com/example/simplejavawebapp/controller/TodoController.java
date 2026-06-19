package com.example.simplejavawebapp.controller;

import com.example.simplejavawebapp.model.Todo;
import com.example.simplejavawebapp.service.TodoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.findAll());
        model.addAttribute("newTodo", new Todo());
        return "todos";
    }

    @PostMapping("/todos")
    public String createTodo(Todo todo) {
        todoService.create(todo.getTitle(), todo.getDescription());
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/edit")
    public String editTodo(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid TODO id: " + id));

        model.addAttribute("todo", todo);
        return "edit-todo";
    }

    @PostMapping("/todos/{id}")
    public String updateTodo(@PathVariable Long id, Todo todo) {
        todoService.update(id, todo.getTitle(), todo.getDescription(), todo.isCompleted());
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/toggle")
    public String toggleTodo(@PathVariable Long id) {
        todoService.toggleCompleted(id);
        return "redirect:/todos";
    }

    @PostMapping("/todos/{id}/delete")
    public String deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return "redirect:/todos";
    }
}