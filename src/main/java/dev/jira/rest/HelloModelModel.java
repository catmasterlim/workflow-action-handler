package dev.jira.rest;

import javax.xml.bind.annotation.*;
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class HelloModelModel {

    @XmlElement(name = "value")
    private String message;

    public HelloModelModel() {
    }

    public HelloModelModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}