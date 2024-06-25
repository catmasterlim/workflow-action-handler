package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;



public class WorkflowActionItemEntityFactory {

    public static WorkflowActionConditionEntity createConditionEntity(WorkflowActionFilterModel filter,  ConditionDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){

        WorkflowActionConditionEntity entity = new WorkflowActionConditionEntity(descriptor, workflow, order, transitionId);
        
        entity.isFiltered = false;
        if( filter.isFilteredActionType(WorkflowActionType.Condition) ){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionClassType(entity.classType)){
            entity.isFiltered = true;
        }

        return entity;
    }
    public static WorkflowActionValidatorEntity createValidatorEntity(WorkflowActionFilterModel filter,  ValidatorDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){



        WorkflowActionValidatorEntity entity = new WorkflowActionValidatorEntity(descriptor, workflow, order, transitionId);

        entity.isFiltered = false;
        if( filter.isFilteredActionType(WorkflowActionType.Validator) ){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionClassType(entity.classType)){
            entity.isFiltered = true;
        }

        return entity;
    }
    public static WorkflowActionPostFunctionEntity createPostFunctionEntity(WorkflowActionFilterModel filter,  FunctionDescriptor descriptor, JiraWorkflow workflow, int order, int transitionId){

        WorkflowActionPostFunctionEntity entity = new WorkflowActionPostFunctionEntity(descriptor, workflow, order, transitionId);

        entity.isFiltered = false;
        if( filter.isFilteredActionType(WorkflowActionType.PostFunction) ){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }
        if ( filter.isFilteredActionClassType(entity.classType)){
            entity.isFiltered = true;
        }

        return entity;
    }
    
}
