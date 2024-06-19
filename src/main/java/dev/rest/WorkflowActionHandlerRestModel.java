package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionHandlerRestModel {

    @XmlElement(name = "workflowName")
    private String workflowName;

    @XmlElement(name = "isDraft")
    public boolean isDraft;

    @XmlElement(name = "workflowActions")
    public List<WorkflowActionHandlerSearchEntity> workflowActions;

    public WorkflowActionHandlerRestModel() {
    }


    public WorkflowActionHandlerRestModel(String workflowName, boolean isDraft, JiraWorkflow workflow) {
        this.workflowName = workflowName;
        this.isDraft = isDraft;
        Collection<ActionDescriptor> actions = workflow.getAllActions();

        workflowActions = new ArrayList<>();

        for(ActionDescriptor action : actions){
            for(Object post :  action.getPostFunctions() ){
                this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "POST", action));
            }
            for(Object post :  action.getPreFunctions() ){
                this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "PRE", action));
            }
            for(Object post :  action.getConditionalResults() ){
                this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "CONDITION", action));
            }
            for(Object post :  action.getValidators() ){
                this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "Validator", action));
            }

        }

    }
}