package dev.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkflowActionFilterModel {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionFilterModel.class);

    private Set<String> actionNames;
    private Set<WorkflowActionType> actionTypes;


    public boolean isFilteredActionType(){
        if( this.actionTypes == null || this.actionTypes.size() == 0){
            return false;
        }

        return true;
    }
    public boolean isFilteredActionType(WorkflowActionType actionType){
        if( this.actionTypes == null || this.actionTypes.size() == 0){
            return false;
        }

        if(this.actionTypes.contains(actionType)){
            return false;
        }

        return true;
    }

    public void addFilterActionType(WorkflowActionType actionType){
        this.actionTypes.add(actionType);
    }
    public void addFilterActionType(String actionType){
        WorkflowActionType type = WorkflowActionType.valueOf(actionType);
        this.actionTypes.add( type );
    }
    public void addFilterActionTypeAll(List<String> actionTypes){
        if(actionTypes==null){
            return;
        }
        try{
            for(String actionType: actionTypes){
                this.addFilterActionType(actionType);
            }
        }catch(Exception ex){
            log.warn("ActionType Filter : {}", actionTypes );
        }
    }

    public boolean isFilteredActionName(){
        if( this.actionNames == null || this.actionNames.size() == 0){
            return false;
        }

        return true;
    }
    public boolean isFilteredActionName(String actionName){
        if( this.actionNames == null || this.actionNames.size() == 0){
            return false;
        }

        for(String filterActionName : this.actionNames){
            if(filterActionName.indexOf(actionName) > -1){
                return false;
            }
        }
        
        return true;
    }
    public void addFilterActionName(String actionName){
        this.actionNames.add(actionName);
    }
    public void addFilterActionNameAll(List<String> actionNames){
        if(actionNames==null){
            return;
        }
        try{
            for(String item : actionNames){
                this.addFilterActionName(item);
            }
        }catch(Exception ex){
            log.warn("ActionName Filter : {}", actionNames );
        }
    }

    public WorkflowActionFilterModel(){

        this.actionNames = new HashSet();
        this.actionTypes = new HashSet();

    }
}
