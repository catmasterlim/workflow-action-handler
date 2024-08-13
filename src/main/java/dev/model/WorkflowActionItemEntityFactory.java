package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;



public class WorkflowActionItemEntityFactory {

    WorkflowTransitionModel transitionModel;
    JiraWorkflow workflow;
    String workflowMode;
    WorkflowActionFilterModel filter;
    
    public WorkflowActionItemEntityFactory(JiraWorkflow workflow, String workflowMode, WorkflowActionFilterModel filter){
        this.transitionModel = new WorkflowTransitionModel(workflow, workflowMode);
        this.workflow = workflow;
        this.workflowMode = workflowMode;
        this.filter = filter;
    }
    
    public WorkflowActionConditionEntity createConditionEntity( ConditionDescriptor descriptor, int order, int transitionId){

        WorkflowActionConditionEntity entity = new WorkflowActionConditionEntity(descriptor, this.workflow, order, transitionId);
        
        entity.isFiltered = false;
        if( this.filter.isFilteredActionType(WorkflowActionType.Condition) ){
            entity.isFiltered = true;
        }
        if ( this.filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }
        if ( this.filter.isFilteredTransitionId(entity.transitionId)){
            entity.isFiltered = true;
        }
        WorkflowTransitionEntity transitionEntity = this.transitionModel.getTransitionEntity(entity.transitionId);
        if ( this.filter.isFilteredTransitionName(transitionEntity.name)){
            entity.isFiltered = true;
        }

        return entity;
    }
    public WorkflowActionValidatorEntity createValidatorEntity(ValidatorDescriptor descriptor, int order, int transitionId){
        
        WorkflowActionValidatorEntity entity = new WorkflowActionValidatorEntity(descriptor, this.workflow, order, transitionId);

        entity.isFiltered = false;
        if( this.filter.isFilteredActionType(WorkflowActionType.Validator) ){
            entity.isFiltered = true;
        }
        if ( this.filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }

        if ( this.filter.isFilteredTransitionId(entity.transitionId)){
            entity.isFiltered = true;
        }
        WorkflowTransitionEntity transitionEntity = this.transitionModel.getTransitionEntity(entity.transitionId);
        if ( this.filter.isFilteredTransitionName(transitionEntity.name)){
            entity.isFiltered = true;
        }

        return entity;
    }
    public WorkflowActionPostFunctionEntity createPostFunctionEntity(FunctionDescriptor descriptor, int order, int transitionId){

        WorkflowActionPostFunctionEntity entity = new WorkflowActionPostFunctionEntity(descriptor, this.workflow, order, transitionId);

        entity.isFiltered = false;
        if( this.filter.isFilteredActionType(WorkflowActionType.PostFunction) ){
            entity.isFiltered = true;
        }
        if ( this.filter.isFilteredActionName(entity.name)){
            entity.isFiltered = true;
        }
        if ( this.filter.isFilteredTransitionId(entity.transitionId)){
            entity.isFiltered = true;
        }
        WorkflowTransitionEntity transitionEntity = this.transitionModel.getTransitionEntity(entity.transitionId);
        if ( this.filter.isFilteredTransitionName(transitionEntity.name)){
            entity.isFiltered = true;
        }

        return entity;
    }
    
}
