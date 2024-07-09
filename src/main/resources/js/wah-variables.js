define('jira-workflow-action-handler/variables', [], function () {

    // make sure that the template namespace is in place
    AJS.namespace("JIRA.WorkflowActionHandler.Variables");

    return JIRA.WorkflowActionHandler.Variables;
});

AJS.namespace("JIRA.WorkflowActionHandler.Variables", null, require('jira-workflow-action-handler/variables'));