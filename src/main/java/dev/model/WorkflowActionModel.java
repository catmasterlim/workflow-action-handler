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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlRootElement(name = "actions")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowActionModel {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionModel.class);


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

    @XmlElement(name = "test")
    public String test;

    public WorkflowActionModel() {
    }

    public WorkflowActionModel(JiraWorkflow workflow, boolean isDraft, WorkflowActionFilterModel filterModel ) {

        this.name = workflow.getName();
        this.isDraft = isDraft;
        this.description = workflow.getDescription();
        // transitions = actions.stream().map( transition -> new WorkflowTransitionEntity(transition, workflow) ).collect(Collectors.toList());
        this.test = "";

        actions = new ArrayList<>();
        
        for(ActionDescriptor transition :  workflow.getAllActions()){
            WorkflowTransitionEntity transitionEntity = new WorkflowTransitionEntity(transition, workflow);

            // log.warn("test trid : {}, actionType : {},  isFiltered : {} " , transitionEntity.id, WorkflowActionType.Condition, filterModel.isFilteredActionType(WorkflowActionType.Condition));
            // Conditions
            if(!filterModel.isFilteredActionType(WorkflowActionType.Condition)){
                List conditions = transition.getRestriction() == null ? new ArrayList<>() : transition.getRestriction().getConditionsDescriptor().getConditions();
                int conditionOrder = 0;
                for( Object obj : conditions){
                    ConditionDescriptor descriptor = (ConditionDescriptor)obj;
                    actions.add(new WorkflowActionConditionEntity(descriptor, workflow, conditionOrder, transitionEntity.id ));
                    conditionOrder++;
                }
            }

            // Validators
            if(!filterModel.isFilteredActionType(WorkflowActionType.Validator)){
                int validatorOrder = 0;
                for( Object obj : transition.getValidators()){
                    ValidatorDescriptor descriptor = (ValidatorDescriptor)obj;
                    actions.add(new WorkflowActionValidatorEntity(descriptor, workflow, validatorOrder, transitionEntity.id ));
                    validatorOrder++;
                }
            }

            // PostFunctions
            if(!filterModel.isFilteredActionType(WorkflowActionType.PostFunction)){
                int postfunctionOrder = 0;
                for( FunctionDescriptor descriptor : workflow.getPostFunctionsForTransition(transition)){
                    actions.add(new WorkflowActionPostFunctionEntity(descriptor, workflow, postfunctionOrder, transitionEntity.id ));
                    postfunctionOrder++;
                }
            }
        }


    }
}