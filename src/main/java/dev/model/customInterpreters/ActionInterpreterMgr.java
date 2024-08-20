package dev.model.customInterpreters;

import dev.model.WorkflowActionEntity;

import java.util.ArrayList;
import java.util.List;

public class ActionInterpreterMgr {
    private ActionInterpreterMgr(){}

    private static ActionInterpreterMgr inst = new ActionInterpreterMgr();
    public static ActionInterpreterMgr Inst(){

        if(inst == null){
            inst = new ActionInterpreterMgr();
            inst.init();
        }

        return inst;
    }

    private List<ActionInterpreter> actionInterpreters = new ArrayList<>();


    private void init(){
        actionInterpreters.add( new ScriptrunnerGroovyCondition());
    }

    /**
     * action interpreter
     * @param entity WorkflowActionEntity
     * @return true/false
     */
    public ActionInterpreter getInterperter(WorkflowActionEntity entity){
        if( entity == null || entity.className == null ){
            return null;
        }

        for(ActionInterpreter interpreter : actionInterpreters){
            if( interpreter.has(entity) ){
                return interpreter;
            }
        }

        return null;
    }


}
