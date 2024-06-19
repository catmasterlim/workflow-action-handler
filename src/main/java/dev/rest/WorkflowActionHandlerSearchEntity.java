package dev.rest;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.opensymphony.workflow.loader.ActionDescriptor;

@XmlRootElement(name = "WorkflowActionHandlerSearchEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionHandlerSearchEntity {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "type")
    public String type;

    @XmlElement(name = "order")
    public int order;

    @XmlElement(name = "class name")
    public String className;

    @XmlElement(name = "transition name")
    public String transitionName;

    public WorkflowActionHandlerSearchEntity(){

    }

    public WorkflowActionHandlerSearchEntity(Object post, String type, ActionDescriptor action){
        try {
            Method method = post.getClass().getMethod("name", null);
            this.name = method.getName().toString();
        } catch (Exception e) {
            this.name = post.toString();
        }
        this.type = type;
        this.className = post.getClass().getName();
        this.transitionName = action.getName();
    }
}
