package dev.model;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.*;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;

@XmlRootElement(name = "Validator")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionValidatorEntity {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "classSimpleName")
    public String classSimpleName;

    @XmlElement(name = "type")
    public final WorkflowActionType type = WorkflowActionType.Validator;

    @XmlElement(name = "order")
    public int order;

    @XmlElement(name = "transitionId")
    public int transitionId;

    @XmlElement(name = "args")
    public Map args;

    @XmlElement(name = "asXML")
    public String asXML;


    public WorkflowActionValidatorEntity(ValidatorDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){

        this.id= descriptor.getId();
        this.name = descriptor.getName();
        this.className = (String)descriptor.getArgs().get("class.name");
        this.classSimpleName = this.className.substring(this.className.lastIndexOf(".")+1);
        this.order = order;
        this.transitionId = transitionId;

        this.args = descriptor.getArgs();
        this.asXML = descriptor.asXML();

    }


}
