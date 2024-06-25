package dev.model;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

@XmlRootElement(name = "WorkflowActionEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionEntity {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "transition")
    public WorkflowTransitionEntity transitionEntity;


    public WorkflowActionEntity(WorkflowTransitionEntity transitionEntity){

        // this.id= transition.getId();
        // this.name = transition.getName();
        // this.className = transition.getClass().getName();

        this.transitionEntity = transitionEntity;

    }

}
