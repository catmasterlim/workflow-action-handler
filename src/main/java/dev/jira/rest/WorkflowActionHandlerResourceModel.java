package dev.jira.rest;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.*;
import com.atlassian.jira.workflow.edit.Workflow;

import com.opensymphony.workflow.loader.ActionDescriptor;


@XmlRootElement(name = "workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionHandlerResourceModel {

    @XmlElement(name = "workflowName")
    private String workflowName;

    @XmlElement(name = "isDraft")
    public boolean isDraft;

    @XmlElement(name = "workflowActions")
    public List<WorkflowActionPostModel> workflowActions;


    public WorkflowActionHandlerResourceModel() {
    }

    public WorkflowActionHandlerResourceModel(String workflowName, boolean isDraft, Workflow workflow) {
        this.workflowName = workflowName;
        this.isDraft = isDraft;
        Collection<ActionDescriptor> actions = workflow.getAllActions();

        workflowActions = new ArrayList<>();

        for(ActionDescriptor action : actions){
            for(Object post :  action.getPostFunctions() ){
                this.workflowActions.add( new WorkflowActionPostModel(post, "POST", action));
            }
            for(Object post :  action.getPreFunctions() ){
                this.workflowActions.add( new WorkflowActionPostModel(post, "PRE", action));
            }
            for(Object post :  action.getConditionalResults() ){
                this.workflowActions.add( new WorkflowActionPostModel(post, "CONDITION", action));
            }
            for(Object post :  action.getValidators() ){
                this.workflowActions.add( new WorkflowActionPostModel(post, "Validator", action));
            }

        }
        
    }


}