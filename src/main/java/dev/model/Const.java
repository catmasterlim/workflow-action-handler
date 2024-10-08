package dev.model;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.plugin.ModuleDescriptor;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Const {

    private static class PredefinedInfo {
        String className;
        String name;
        String description;
        String classType;

        public PredefinedInfo setClassName(String className) {
            this.className = className;
            return this;
        }

        public PredefinedInfo setName(String name) {
            this.name = name;
            return this;
        }

        public PredefinedInfo setDescription(String description) {
            this.description = description;
            return this;
        }

        public PredefinedInfo setClassType(String classType) {
            this.classType = classType;
            return this;
        }


        //
        PredefinedInfo(String className, String name, String description){
            this.className = className;
            this.name = name;
            this.description = description;
        }

    }

    private static final Logger log = LoggerFactory.getLogger(Const.class);


    private static com.atlassian.plugin.PluginAccessor pluginAccessor;
    private static  com.atlassian.jira.config.ResolutionManager resolutionManager;
    private static  com.atlassian.jira.config.ConstantsManager constantsManager;
    private static  com.atlassian.jira.issue.CustomFieldManager customFieldManager;
    private static  com.atlassian.jira.issue.customfields.manager.OptionsManager optionsManager;
    public static void setPluginAccessor(PluginAccessor pluginAccessor) {
        Const.pluginAccessor = pluginAccessor;
    }
    public static void setResolutionManager(com.atlassian.jira.config.ResolutionManager resolutionManager) {
        Const.resolutionManager = resolutionManager;
    }
    public static void setConstantsManager(com.atlassian.jira.config.ConstantsManager constantsManager) {
        Const.constantsManager = constantsManager;
    }
    public static void setCustomFieldManager(com.atlassian.jira.issue.CustomFieldManager customFieldManager) {
        Const.customFieldManager = customFieldManager;
    }
    public static void setOptionsManager(com.atlassian.jira.issue.customfields.manager.OptionsManager optionsManager) {
        Const.optionsManager = optionsManager;
    }


    private static List<String> systemClassList = Arrays.asList(
            "com.atlassian.jira.workflow.function.issue.IssueCreateFunction"
            , "com.atlassian.jira.workflow.function.issue.IssueReindexFunction"
            , "com.atlassian.jira.workflow.function.event.FireIssueEventFunction"
            , "com.atlassian.jira.workflow.function.issue.UpdateIssueStatusFunction"
            , "com.atlassian.jira.workflow.function.misc.CreateCommentFunction"
            , "com.atlassian.jira.workflow.function.issue.GenerateChangeHistoryFunction"
    );

    public static boolean isSystemClassType(String classFullName){
        if(classFullName == null || classFullName.isEmpty() ){
            return false;
        }
        return systemClassList.contains(classFullName);
    }
    public static boolean isBundledClassType(String classFullName){
        if(classFullName == null || classFullName.isEmpty() ){
            return false;
        }

        if( mapPredefinedConditionInfo.get(classFullName) != null ){
            return true;
        }
        if( mapPredefinedValidatorInfo.get(classFullName) != null ){
            return true;
        }
        if( mapPredefinedPostFunctionInfo.get(classFullName) != null ){
            return true;
        }

        return false;

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

    private static final Map<String, PredefinedInfo > mapPredefinedValidatorInfo ;
    static {
        mapPredefinedValidatorInfo = new HashMap<>();
        PredefinedInfo predefinedInfo = null;
        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.validator.PermissionValidator"
                ,"Permission Validator"
                , "Validates that the user has a permission."
        );
        mapPredefinedValidatorInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.validator.UserPermissionValidator"
                ,"User Permission Validator"
                , "Validates that the user has a permission, where the OSWorkflow variable holding the username is configurable. Obsolete. "
        );
        mapPredefinedValidatorInfo.put(predefinedInfo.className, predefinedInfo);

        /**
         Permission Validator 	Validates that the user has a permission.
         User Permission Validator 	Validates that the user has a permission, where the OSWorkflow variable holding the username is configurable. Obsolete.
         */

    }

    private static final Map<String, PredefinedInfo > mapPredefinedPostFunctionInfo ;
    static {
        mapPredefinedPostFunctionInfo = new HashMap<>();
        PredefinedInfo predefinedInfo = null;

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.function.issue.AssignToCurrentUserFunction"
                ,"Assign to Current User"
                , "Assigns the issue to the current user if the current user has the 'Assignable User' permission."
        );
        mapPredefinedPostFunctionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.function.issue.AssignToLeadFunction"
                ,"Assign to Lead Developer"
                , "Assigns the issue to the project/component lead developer."
        );
        mapPredefinedPostFunctionInfo.put(predefinedInfo.className, predefinedInfo);

        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.function.issue.AssignToReporterFunction"
                ,"Assign to Reporter"
                , "Assigns the issue to the reporter"
        );
        mapPredefinedPostFunctionInfo.put(predefinedInfo.className, predefinedInfo);


        predefinedInfo = new PredefinedInfo(
                "com.atlassian.jira.workflow.function.issue.UpdateIssueFieldFunction"
                ,"Update Issue Field"
                , "Updates a simple issue field to a given value."
        );
        mapPredefinedPostFunctionInfo.put(predefinedInfo.className, predefinedInfo);


        /**

         Assign to Current User 	Assigns the issue to the current user if the current user has the 'Assignable User' permission.
         Assign to Lead Developer 	Assigns the issue to the project/component lead developer
         Assign to Reporter 	Assigns the issue to the reporter
         Trigger a Webhook 	If this post-function is executed, Jira will post the issue content in JSON format to the URL specified.
         Update Issue Field 	Updates a simple issue field to a given value.

         */

    }

    private static PredefinedInfo getPredefinedInfo(WorkflowActionEntity entity){
        WorkflowActionType actionType = entity.type;
        switch (actionType){
            case Condition:
                return mapPredefinedConditionInfo.get(entity.className);
            case Validator:
                return mapPredefinedValidatorInfo.get(entity.className);
            case PostFunction:
                return mapPredefinedPostFunctionInfo.get(entity.className);
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


    public static String getFieldValueDisplay(WorkflowActionEntity entity){
        String filed = (String) entity.args.get("field.name");
        String value = (String) entity.args.get("field.value");

        switch (filed){
            case "resolution" :{
                if (value != null && value.isBlank() != true) {
                    com.atlassian.jira.issue.resolution.Resolution resolution = Const.resolutionManager.getResolution(value);
                    if (resolution != null) {
                        return resolution.getName() + "(" + value + ")";
                    }
                }
            }
            return "";
        }

        return value;
    }


    /**
     * arg 화면에 표시되는 형태가 변경되는 경우
     * "_display" 키로 args map에 넣는다
     */
    final static String displayKey = "_display_";
    public static WorkflowActionEntity addArgDisplyed(WorkflowActionEntity entity){


        if( entity.args == null ){
            return entity;
        }

        entity.args.remove(displayKey);
        if( entity.args.size() < 2 ){
            return entity;
        }

        switch (entity.className){
            case "com.atlassian.jira.workflow.function.issue.UpdateIssueFieldFunction": {
                String display = getFieldValueDisplay(entity);
                entity.args.put(displayKey, display);

            }
                break;
            case "com.atlassian.jira.plugins.webhooks.workflow.TriggerWebhookFunction": {
                String value = (String) entity.args.get("field.webhookId");
                if (value != null && value.isBlank() != true) {
                    com.atlassian.jira.issue.resolution.Resolution resolution = Const.resolutionManager.getResolution(value);
                    if (resolution != null) {
                        entity.args.put(displayKey, resolution.getName() + "(" + value + ")");
                    } else {
                        entity.args.put(displayKey, "Unknown" + "(" + value + ")");
                    }

                } else {
                    entity.args.put(displayKey, "");
                }
            }
                break;
            default:
                break;
        }

        return entity;
    }

    public static Optional<Plugin> getPlugin(WorkflowActionEntity entity){
        Map<String, String> mapPackage = new HashMap<>();

        //
        mapPackage.put("ch.beecom.jira.jsu.workflow.", "com.googlecode.jira-suite-utilities");
        mapPackage.put("com.onresolve.jira.", "com.onresolve.jira.groovy.groovyrunnerscriptrunner-workflow-function-com");
        mapPackage.put("com.onresolve.scriptrunner.", "com.onresolve.jira.groovy.groovyrunnerscriptrunner-workflow-function-com");

        //
        Collection<Plugin> plugins = pluginAccessor.getPlugins();

        String fullmodulekey = entity.getModuleKey();
        if( fullmodulekey == null || fullmodulekey.isEmpty() ){
            Optional<String> finded = mapPackage.keySet().stream().filter( it -> entity.className.startsWith(it) ).findFirst();
            if( finded.isPresent() ){
                fullmodulekey = mapPackage.get( finded.get() );
            } else {
                log.trace(" plugin trace - not find : {}", entity.className);
            }
        }
        final String entityModuleKey =  (String)( fullmodulekey == null || fullmodulekey.isEmpty() ? entity.className : fullmodulekey) ;
        Optional<Plugin> plugin = plugins.stream().filter(p ->  entityModuleKey.startsWith(p.getKey())  || p.getModuleDescriptor(entityModuleKey) != null ).findFirst();
        return plugin;
    }

    public static void injectPredefinedTypeInfo(WorkflowActionEntity entity){

        // predefined
        PredefinedInfo predefinedInfo = getPredefinedInfo(entity);
        if( predefinedInfo == null ){
            //String className, String name, String description
            predefinedInfo = new PredefinedInfo(entity.className, entity.classSimpleName, "");

        }

        // plugin info
        log.trace("injectPredefinedTypeInfo - entity className : {} ", entity.className);
        entity.plugin =  WorkflowPluginEntity.Create(entity.className, getPlugin(entity));

        // TODO : Bundled Plugin 이 아닌 모든 plugin은 임시로 Commercial Plugin로 표시
        if( entity.plugin.isBundledPlugin == false ){
            entity.plugin.name = "Commercial Plugin";
            entity.plugin.key = "CommercialPlugin";
        }

        // args
        addArgDisplyed(entity);

        entity.name = predefinedInfo.name;
        entity.description = predefinedInfo.description;
    }

}
