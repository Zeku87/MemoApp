package com.hercudev.zeku.memo;

/**
 * En esta clase es donde encapsularemos y gestionamos cada objeto Note.
 *
 * Let's create a class where we can encapsulate all the attributes and methods that a note can have.
 *
 * Created by zeku87 on 12/01/17.
 */
public class Note {

    private String title; //
    private String description;
    private Boolean idea;
    private Boolean todo;
    private Boolean important;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIdea() {
        return idea;
    }

    public void setIdea(Boolean idea) {
        this.idea = idea;
    }

    public Boolean isTodo() {
        return todo;
    }

    public void setTodo(Boolean todo) {
        this.todo = todo;
    }

    public Boolean isImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }
}
