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
import com.opensymphony.workflow.loader.ConditionDescriptor;

@XmlRootElement(name = "Condition")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionConditionEntity {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "classSimpleName")
    public String classSimpleName;

    @XmlElement(name = "type")
    public final WorkflowActionType type = WorkflowActionType.Condition;

    @XmlElement(name = "order")
    public int order;

    @XmlElement(name = "transitionId")
    public int transitionId;

    @XmlElement(name = "args")
    public Map args;

    @XmlElement(name = "asXML")
    public String asXML;


    public WorkflowActionConditionEntity(ConditionDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){

        this.id= descriptor.getId();
        this.name = descriptor.getName();
        this.className = (String)descriptor.getArgs().get("class.name");
        // this.className = (String)descriptor.getClass().getName();
        this.classSimpleName = this.className.substring(this.className.lastIndexOf(".")+1);
        this.order = order;
        this.transitionId = transitionId;

        this.args = descriptor.getArgs();
        this.asXML = descriptor.asXML();

    }


}
