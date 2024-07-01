package dev.model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ClassTypeFactory {
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
    public static String create(String classFullName){

        if( isDefaultClassType(classFullName) ) {
            return "Default";
        }

        if( isJiraBaseClassType(classFullName)){
            return "JiraBase";
        }

        // TODO - com.atlassian.jira.plugin.JiraPluginManager


        return "Custom";
    }
}
