package dev.rest;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

@XmlRootElement(name = "ActionEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActionEntity {

    @XmlElement(name = "id")
    private int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "transition")
    public ActionTransitionEntity transitionEntity;


    public ActionEntity(ActionTransitionEntity transitionEntity){

        // this.id= transition.getId();
        // this.name = transition.getName();
        // this.className = transition.getClass().getName();

        this.transitionEntity = transitionEntity;

    }


}
