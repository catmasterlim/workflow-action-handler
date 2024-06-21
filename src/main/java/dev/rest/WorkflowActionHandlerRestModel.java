package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.model.*;

@XmlRootElement(name = "workflow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionHandlerRestModel {

    @XmlElement(name = "workflowName")
    private String workflowName;

    @XmlElement(name = "isDraft")
    public boolean isDraft;

    @XmlElement(name = "countActions")
    public int countAction;

    @XmlElement(name = "workflowActions")
    public List<WorkflowTransitionEntity> workflowActions;

    public WorkflowActionHandlerRestModel() {
    }


    public WorkflowActionHandlerRestModel(String workflowName, boolean isDraft, JiraWorkflow workflow) {
        this.workflowName = workflowName;
        this.isDraft = isDraft;
        Collection<ActionDescriptor> transitions = workflow.getAllActions();

        workflowActions = new ArrayList<>();

        this.countAction = transitions.size();

        for(ActionDescriptor transition : transitions){
            this.workflowActions.add(new WorkflowTransitionEntity(transition, workflow));
            // for(Object item :  transition.getAllActions() ){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntityPost(item, transition));
            // }
   
            // for(Object post :  action.getPreFunctions() ){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "PRE", action));
            // }
            // for(Object post :  action.getConditionalResults() ){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "CONDITION", action));
            // }
            // for(Object post :  action.getValidators() ){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntity(post, "Validator", action));
            // }
            // for(StepDescriptor stepDescriptor :  workflow.getStepsForTransition(transition)){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntity(stepDescriptor, "Step", transition));
            // }

            // for(FunctionDescriptor functionDescriptor :  workflow.getPostFunctionsForTransition(action)){
            //     this.workflowActions.add( new WorkflowActionHandlerSearchEntity(functionDescriptor, "POST", action));
            // }

        }

    }
}