package dev.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkflowActionFilterModel {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionFilterModel.class);

    private final Set<String> actionNames = new HashSet<>();
    private final Set<WorkflowActionType> actionTypes = new HashSet<>();
    private final Set<String> actionClassType = new HashSet<>();

    public boolean isFilteredActionType(WorkflowActionType actionType){
        if( this.actionTypes == null || this.actionTypes.isEmpty()){
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
    public void addFilterActionType(String at){
        WorkflowActionType type = WorkflowActionType.valueOf(at);
        this.actionTypes.add( type );
    }
    public void addFilterActionTypeAll(List<String> list){
        if(list==null){
            return;
        }
        try{
            for(String item: list){
                this.addFilterActionType(item);
            }
        }catch(Exception ex){
            log.warn("ActionType Filter : {}", list );
        }
    }

    public boolean isFilteredActionName(String actionName){
        if( this.actionNames == null || this.actionNames.isEmpty()){
            return false;
        }

        for(String filterActionName : this.actionNames){
            if(filterActionName.contains(actionName)){
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

    public boolean isFilteredActionClassType(String at){
        if( this.actionClassType == null || this.actionClassType.isEmpty()){
            return false;
        }

        for(String filterActionClassType: this.actionClassType){
            if(filterActionClassType.contains(at)){
                return false;
            }
        }

        return true;
    }
    public void addFilterActionClassType(String at){
        this.actionClassType.add(at);
    }
    public void addFilterActionClassTypeAll(List<String> list){
        if(list==null){
            return;
        }
        try{
            for(String item : list){
                this.addFilterActionClassType(item);
            }
        }catch(Exception ex){
            log.warn("Action Class Type Filter : {}", list );
        }
    }

    public WorkflowActionFilterModel(){


    }
}
