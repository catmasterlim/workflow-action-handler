define('jira-workflow-action-handler/templates', [], function () {

    // console.log('----> jira-workflow-action-handler/templates');
    // make sure that the template namespace is in place
    AJS.namespace("JIRA.WorkflowActionHandler.Templates");
    return JIRA.WorkflowActionHandler.Templates;
});

AJS.namespace("JIRA.WorkflowActionHandler.Templates", null, require('jira-workflow-action-handler/templates'));
