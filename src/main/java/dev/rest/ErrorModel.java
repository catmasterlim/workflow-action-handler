package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.ErrorManager;
import java.util.stream.Collectors;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorModel {

    @XmlElement(name = "code")
    private int code;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "content")
    public String content ;


    public ErrorModel() {
        this.name = "Unknown";
        this.code = 500;
        this.description = "알 수 없는 에러";
        this.content = "";
    }

    public ErrorModel(int code, String name, String description, String content) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.content = content;
    }

    
}