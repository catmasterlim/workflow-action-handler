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

@XmlRootElement(name = "PostFunction")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionPostFunctionEntity extends WorkflowActionEntity {


    public WorkflowActionPostFunctionEntity(FunctionDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){
        super(WorkflowActionType.PostFunction);

        this.id= descriptor.getId();
        this.className = ((String)descriptor.getArgs().get("class.name")).trim();
        this.classSimpleName = this.className.substring(this.className.lastIndexOf(".")+1);
        this.name = descriptor.getName();
        this.order = order;
        this.transitionId = transitionId;
        this.args = new HashMap(descriptor.getArgs());
        this.countArgs = this.args.size();
        this.asXML = descriptor.asXML();
        this.isFiltered = false;

        Const.injectPredefinedTypeInfo(this);
    }


}
