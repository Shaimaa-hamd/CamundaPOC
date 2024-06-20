package com.example.workflow;

public class TaskDto {
    private String id;
    private String name;
    private String formKey;

    public TaskDto(String id, String name, String formKey) {
        this.id = id;
        this.name = name;
        this.formKey = formKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }
}