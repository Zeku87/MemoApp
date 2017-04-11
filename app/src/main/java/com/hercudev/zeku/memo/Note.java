package com.hercudev.zeku.memo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * En esta clase es donde encapsularemos y gestionamos cada objeto Note.
 *
 * De cara a la serialización de objetos de esta clase producida en la clase JSONSerialization.java
 * definimos un método convertToJSON() para la serialización y un segundo constructor
 * que recibirá los objetos deserializados para cargarlos en memoria principal
 *
 * Los atributos relacionados con esta actividad se usan como una clave.
 *
 * Created by Zeku on 12/01/17.
 */
public class Note {

    //Atributos usados como clave
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_IDEA = "idea";
    private static final String JSON_TODO = "todo";
    private static final String JSON_IMPORTANT = "important";

    private String title;
    private String description;
    private Boolean idea;
    private Boolean todo;
    private Boolean important;

    public Note(){}

    public Note (JSONObject jsonObject) throws JSONException{
        title = jsonObject.getString(JSON_TITLE);
        description = jsonObject.getString(JSON_DESCRIPTION);
        idea = jsonObject.getBoolean(JSON_IDEA);
        todo = jsonObject.getBoolean(JSON_TODO);
        important = jsonObject.getBoolean(JSON_IMPORTANT);
    }

    public JSONObject convertToJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(JSON_TITLE, title);
        jsonObject.put(JSON_DESCRIPTION, description);
        jsonObject.put(JSON_TODO, todo);
        jsonObject.put(JSON_IDEA, idea);
        jsonObject.put(JSON_IMPORTANT, important);

        return jsonObject;
    }

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
