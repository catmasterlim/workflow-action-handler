package dev.model;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import dev.rest.WorkflowActionHandlerRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Const {

    private static class PredefinedInfo {
        String className;
        String name;
        String description;
        String classType;
        PredefinedInfo(String className, String name, String description, String classType){
            this.className = className;
            this.name = name;
            this.description = description;
            this.classType = classType;
        }
        PredefinedInfo(String className, String name, String description){
            this.className = className;
            this.name = name;
            this.description = description;
            this.classType = getClassType(this.className);
        }

    }

    private static final Logger log = LoggerFactory.getLogger(Const.class);


    public static void setPluginAccessor(PluginAccessor pluginAccessor) {
        Const.pluginAccessor = pluginAccessor;
    }

    private static com.atlassian.plugin.PluginAccessor pluginAccessor;

    private static List<String> defaultClassList = Arrays.asList(
            "com.atlassian.jira.workflow.function.issue.IssueCreateFunction"
            , "com.atlassian.jira.workflow.function.issue.IssueReindexFunction"
            , "com.atlassian.jira.workflow.function.event.FireIssueEventFunction"
            , "com.atlassian.jira.workflow.function.issue.UpdateIssueStatusFunction"
            , "com.atlassian.jira.workflow.function.misc.CreateCommentFunction"
            , "com.atlassian.jira.workflow.function.issue.GenerateChangeHistoryFunction"
            , "com.atlassian.jira.workflow.function.misc.CreateCommentFunction"
    );
    public static boolean isDefaultClassType(String classFullName){
        if(classFullName == null || classFullName.isEmpty() ){
            return false;
        }
        if( defaultClassList.contains(classFullName) ) {
            return true;
        }
        return false;
    }
    public static boolean isJiraBaseClassType(String classFullName){
        if(classFullName == null || classFullName.isEmpty() ){
            return false;
        }
        if( classFullName.startsWith("com.atlassian.jira.workflow.")) {
            return true;
        }
        return false;
    }
    public static String getClassType(String classFullName){

        if( isDefaultClassType(classFullName) ) {
            return "Default";
        }

        if( isJiraBaseClassType(classFullName)){
            return "JiraBase";
        }

        // TODO - com.atlassian.jira.plugin.JiraPluginManager

        return "Custom";
    }

    private static final Map<String, PredefinedInfo > mapPredefinedConditionInfo ;
    static {
        mapPredefinedConditionInfo = new HashMap<>();
        PredefinedInfo predefinedInfo = null;
        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.AlwaysFalseCondition"
                ,"Hide transition from user"
                , "Condition to hide a transition from the user. The transition can only be triggered from a workflow function."
                );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.AllowOnlyAssignee"
                ,"Only Assignee Condition"
                , "Condition to allow only the assignee to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.AllowOnlyReporter"
                ,"Only Reporter Condition"
                , "Condition to allow only the reporter to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.AllowOnlyReporter"
                ,"Only Reporter Condition"
                , "Condition to allow only the reporter to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.PermissionCondition"
                ,"Permission Condition"
                , "Condition to allow only users with a certain permission to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.SubTaskBlockingCondition"
                ,"Sub-Task Blocking Condition"
                , "Condition to block parent issue transition depending on sub-task status."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.UserInGroupCondition"
                ,"User Is In Group"
                , "Condition to allow only users in a given group to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.InGroupCFCondition"
                ,"User Is In Group Custom Field"
                , "Condition to allow only users in a custom field-specified group to execute a transition"
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.condition.InProjectRoleCondition"
                ,"User Is In Project Role"
                , "Condition to allow only users in a given project role to execute a transition."
        );
        mapPredefinedConditionInfo.put(predefinedInfo.className, predefinedInfo);

        /**
         Hide transition from user 	Condition to hide a transition from the user. The transition can only be triggered from a workflow function.
         Only Assignee Condition 	Condition to allow only the assignee to execute a transition.
         Only Reporter Condition 	Condition to allow only the reporter to execute a transition.
         Permission Condition 	Condition to allow only users with a certain permission to execute a transition.
         Sub-Task Blocking Condition 	Condition to block parent issue transition depending on sub-task status.
         User Is In Group 	Condition to allow only users in a given group to execute a transition.
         User Is In Group Custom Field 	Condition to allow only users in a custom field-specified group to execute a transition.
         User Is In Project Role 	Condition to allow only users in a given project role to execute a transition.
         */

    }

    private static PredefinedInfo getPredefinedInfo(WorkflowActionEntity entity ){
        WorkflowActionType actionType = entity.type;
        switch (actionType){
            case Condition:
                return mapPredefinedConditionInfo.get(entity.className);
        }

        return null;
    }


    public static boolean isPredefinedType(WorkflowActionEntity entity){
        PredefinedInfo predefinedInfo = getPredefinedInfo(entity);
        if( predefinedInfo == null ){
            return  false;
        }

        return true;
    }


    public static void injectPredefinedTypeInfo(WorkflowActionEntity entity){

        // plugin info
        if(pluginAccessor != null){
            Collection<Plugin> plugins = pluginAccessor.getPlugins();

            Optional<Plugin> plugin = plugins.stream().filter(p ->  entity.className.startsWith(p.getKey())).findFirst();
            if (plugin.isPresent()) {
                entity.plugin = new WorkflowPluginEntity(plugin.get());;
            } else {
                if(entity.className.startsWith("com.atlassian.jira.")){
                    entity.plugin = WorkflowPluginEntity.CreateJiraPluginEntity();
                } else {
                    entity.plugin = WorkflowPluginEntity.CreateUnknownPluginEntity();
                }
            }
        }

        // predefined
        PredefinedInfo predefinedInfo = getPredefinedInfo(entity);
        if( predefinedInfo == null ){
            //String className, String name, String description
            predefinedInfo = new PredefinedInfo(entity.className, entity.classSimpleName, "");
        }

        entity.name = predefinedInfo.name;
        entity.description = predefinedInfo.description;
        if(entity.classType == null|| entity.classType.isEmpty()){
            entity.classType = getClassType(entity.className);
        }

    }

}
