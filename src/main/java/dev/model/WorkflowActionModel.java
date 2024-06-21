package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "actions")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionModel {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "isDraft")
    public boolean isDraft;

    @XmlElement(name = "isActive")
    public boolean isActive;

    @XmlElement(name = "actions")
    public List actions;

    public WorkflowActionModel() {
    }

    public WorkflowActionModel(JiraWorkflow workflow, boolean isDraft) {

        this.name = workflow.getName();
        this.isDraft = isDraft;
        this.description = workflow.getDescription();
        // transitions = actions.stream().map( transition -> new WorkflowTransitionEntity(transition, workflow) ).collect(Collectors.toList());

        actions = new ArrayList<>();

        
        for(ActionDescriptor transition :  workflow.getAllActions()){
            WorkflowTransitionEntity transitionEntity = new WorkflowTransitionEntity(transition, workflow);


            // Conditions
            List conditions = transition.getRestriction() == null ? new ArrayList<>() : transition.getRestriction().getConditionsDescriptor().getConditions();
            int conditionOrder = 0;
            for( Object obj : conditions){
                ConditionDescriptor descriptor = (ConditionDescriptor)obj;
                actions.add(new WorkflowActionConditionEntity(descriptor, workflow, conditionOrder, transitionEntity.id ));
                conditionOrder++;
            }

            // // Validators
            // int validatorOrder = 0;
            // for( Object obj : transition.getValidators()){
            //     ValidatorDescriptor descriptor = (ValidatorDescriptor)obj;
            //     actions.add(new WorkflowActionValidatorEntity(descriptor, workflow, validatorOrder, transitionEntity.id ));
            //     validatorOrder++;
            // }

            // // PostFunctions
            // int postfunctionOrder = 0;
            // for( FunctionDescriptor descriptor : workflow.getPostFunctionsForTransition(transition)){
            //     actions.add(new WorkflowActionPostFunctionEntity(descriptor, workflow, postfunctionOrder, transitionEntity.id ));
            //     postfunctionOrder++;
            // }
        }


    }
}