package dev.model;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
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
public class WorkflowActionConditionEntity extends WorkflowActionEntity  {

    @XmlElement(name = "args")
    public Map args;

    @XmlElement(name = "countArgs")
    public int countArgs;

    @XmlElement(name = "asXML")
    public String asXML;

    @XmlElement(name = "isFiltered")
    public boolean isFiltered;

    @XmlElement(name = "isNegate")
    public boolean isNegate;

    @XmlElement(name = "temp")
    public String temp;



    public WorkflowActionConditionEntity(ConditionDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){
        super(WorkflowActionType.Condition);

        this.id= descriptor.getId();
        this.temp = descriptor.getClass().getName();
        this.className = ((String)descriptor.getArgs().get("class.name")).trim();
        this.classSimpleName = this.className.substring(this.className.lastIndexOf(".")+1);
        this.name = descriptor.getName();
        this.order = order;
        this.transitionId = transitionId;
        this.args = new HashMap(descriptor.getArgs());
        if(this.args.get("statuses") != null){
            String str = (String)this.args.get("statuses");
            this.args.put("statuses", str.split(","));
        }
        this.countArgs = this.args.size();
        this.asXML = descriptor.asXML();
        this.isFiltered = false;
        this.isNegate = descriptor.isNegate();

        Const.injectPredefinedTypeInfo(this);
    }
}
