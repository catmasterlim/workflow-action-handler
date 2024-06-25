package dev.model;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.annotation.*;

import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import com.atlassian.jira.workflow.JiraWorkflow;

import com.opensymphony.workflow.loader.RestrictionDescriptor;



@XmlRootElement(name = "WorkflowTransitionEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowTransitionEntity {

    @XmlElement(name = "id")
    public int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "className")
    public String className;

    @XmlElement(name = "countCondition")
    public int countConditional;

    @XmlElement(name = "countValidatos")
    public int countValidator;

    @XmlElement(name = "countPostFunction")
    public int countPostFunction;

    @XmlElement(name = "countAttribute")
    public int countAttribute;

    @XmlElement(name = "attributes")
    public String attributes;

    @XmlElement(name = "isCommon")
    public boolean isCommon;

    @XmlElement(name = "isFinish")
    public boolean isFinish;

    @XmlElement(name = "isAutoExecute")
    public boolean isAutoExecute;

    @XmlElement(name = "view")
    public String view;

    @XmlElement(name = "isGlobal")
    public boolean isGlobal;

    @XmlElement(name = "isInitialAction")
    public boolean isInitialAction;

    @XmlElement(name = "isOrdinaryAction")
    public boolean isOrdinaryAction;


    public WorkflowTransitionEntity(ActionDescriptor transition, JiraWorkflow workflow){

        this.id= transition.getId();
        this.name = transition.getName();
        this.className = transition.getClass().getName();

        this.countValidator = transition.getValidators().size();
        RestrictionDescriptor restrict = transition.getRestriction();
        this.countConditional = restrict == null ? 0 : restrict.getConditionsDescriptor().getConditions().size();
        this.countPostFunction = workflow.getPostFunctionsForTransition(transition).size();
        this.countAttribute = transition.getMetaAttributes().keySet().size();

        Map map = transition.getMetaAttributes();
        this.attributes = "";
        map.forEach((key, val)->{
            attributes +=  "key : " + key + "," + "value : " + val + " ";
        });

        this.isCommon = transition.isCommon();
        this.isFinish = transition.isFinish();
        this.isAutoExecute = transition.getAutoExecute();
        this.view = transition.getView();
        this.isGlobal = workflow.isGlobalAction(transition);
        this.isInitialAction = workflow.isInitialAction(transition);
        this.isOrdinaryAction = workflow.isOrdinaryAction(transition);
    }


}
