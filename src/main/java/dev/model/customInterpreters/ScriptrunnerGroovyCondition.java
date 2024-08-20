package dev.model.customInterpreters;

import dev.model.WorkflowActionEntity;

import java.util.Base64;

/**
 * com.onresolve.jira.groovy.GroovyCondition
 *
 * sample -
 * RESOLUTION_FIELD_NAME	"10000"
 * canned-script	"com.onresolve.scriptrunner.canned.jira.workflow.conditions.AllSubtasksResolvedCondition"
 * FIELD_FUNCTION_ID	"f7ed3e30-d315-4e29-8c12-541c89a1c629"
 * class.name	"com.onresolve.jira.groovy.GroovyCondition"
 * FIELD_NOTES	"YCFgdGVzdCA="
 */
public class ScriptrunnerGroovyCondition implements ActionInterpreter{
    @Override
    public boolean has(WorkflowActionEntity entity) {
        if("com.onresolve.jira.groovy.GroovyCondition".equals(entity)){
            return true;
        }
        return false;
    }

    @Override
    public WorkflowActionEntity interpreterEntity(WorkflowActionEntity entity) {

        if( "com.onresolve.jira.groovy.GroovyCondition".equals(entity.className) == false ){
            throw new RuntimeException("Can't interperter class : " + entity.className + " at the " + this.getClass().getName());
        }

        if(entity.args == null ){
            throw new RuntimeException("Can't interperter class : " + entity.className + " at the " + this.getClass().getName() + " : args is null");
        }

        String FIELD_NOTES = new String( Base64.getDecoder().decode( (String)entity.args.get("FIELD_NOTES") ) );
//        String FIELD_FUNCTION_ID = (String)entity.args.get("FIELD_FUNCTION_ID");
        String FIELD_SCRIPT_FILE_OR_SCRIPT = new String( Base64.getDecoder().decode( (String)entity.args.get("FIELD_SCRIPT_FILE_OR_SCRIPT") ) );


        entity.args.put("FIELD_NOTES_display_", FIELD_NOTES);
        entity.args.put("FIELD_SCRIPT_FILE_OR_SCRIPT_display_", FIELD_SCRIPT_FILE_OR_SCRIPT);


        return entity;
    }
}
