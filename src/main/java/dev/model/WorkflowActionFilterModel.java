package dev.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkflowActionFilterModel {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionFilterModel.class);

    private String actionName;
    private Set<WorkflowActionType> actionTypes;

    public boolean isFilteredActionName(){
        if( actionName == null || actionName.isEmpty()){
            return false;
        }

        return true;
    }

    public boolean isFilteredActionType(){
        if( actionTypes == null || actionTypes.size() == 0){
            return false;
        }

        return true;
    }
    public boolean isFilteredActionType(WorkflowActionType actionType){
        if( actionTypes == null || actionTypes.size() == 0){
            return false;
        }

        if(actionTypes.contains(actionType)){
            return false;
        }

        return true;
    }

    public void addFilterActionType(WorkflowActionType actionType){
        actionTypes.add(actionType);
    }
    public void addFilterActionType(String actionType){
        WorkflowActionType type = WorkflowActionType.valueOf(actionType);
        actionTypes.add( type );
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

    public WorkflowActionFilterModel(){

        this.actionName = "";
        this.actionTypes = new HashSet();

    }
}
