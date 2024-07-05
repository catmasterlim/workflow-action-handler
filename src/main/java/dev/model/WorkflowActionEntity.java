package dev.model;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.*;

import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

@XmlRootElement(name = "WorkflowActionEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionEntity {

    @XmlElement(name = "id")
    public int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "classSimpleName")
    public String classSimpleName;

    @XmlElement(name = "classType")
    public String classType;

    @XmlElement(name = "type")
    public final WorkflowActionType type;

    @XmlElement(name = "order")
    public int order;

    @XmlElement(name = "transitionId")
    public int transitionId;

    @XmlElement(name = "args")
    public Map args;

    @XmlElement(name = "countArgs")
    public int countArgs;

    @XmlElement(name = "asXML")
    public String asXML;


    @XmlElement(name = "isFiltered")
    public boolean isFiltered;

    @XmlElement(name = "plugin")
    public WorkflowPluginEntity plugin;


    public WorkflowActionEntity(WorkflowActionType type){
        this.type = type;
    }

}
