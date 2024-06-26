package dev.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


public class WorkflowActionFilterModel {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionFilterModel.class);

    private final Set<String> actionNames = new HashSet<>();
    private final Set<WorkflowActionType> actionTypes = new HashSet<>();
    private final Set<String> actionClassType = new HashSet<>();
    private final Set<Integer> transitionId = new HashSet<>();
    private final Set<String> transitionName = new HashSet<>();

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

    public boolean isFilteredTransitionId(Integer at){
        if( this.transitionId == null || this.transitionId.isEmpty()){
            return false;
        }

        for(Integer item: this.transitionId){
            if(item == at){
                return false;
            }
        }

        return true;
    }
    public void addFilterTransitionId(Integer at){
        this.transitionId.add(at);
    }
    public void addFilterTransitionIdAll(List<Integer> list){
        if(list==null){
            return;
        }
        try{
            for(Integer item : list){
                this.addFilterTransitionId(item);
            }
        }catch(Exception ex){
            log.warn("Transition Id Filter : {}", list );
        }
    }

    public boolean isFilteredTransitionName(String at){
        if( this.transitionName == null || this.transitionName.isEmpty()){
            return false;
        }

        for(String item: this.transitionName){
            if(item.contains(at)){
                return false;
            }
        }

        return true;
    }
    public void addFilterTransitionName(String at){
        this.transitionName.add(at);
    }
    public void addFilterTransitionNameAll(List<String> list){
        if(list==null){
            return;
        }
        try{
            for(String item : list){
                this.addFilterTransitionName(item);
            }
        }catch(Exception ex){
            log.warn("Transition Name Filter : {}", list );
        }
    }

    public WorkflowActionFilterModel(){


    }
}
