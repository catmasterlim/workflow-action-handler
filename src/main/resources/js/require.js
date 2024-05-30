

define("workflow-action-handler/application-loader", [
    "wrm/require",
    "jquery",
    "require"
], function(
    wrmRequire,
    jQuery,
    require
) {
    return {
  
        load: function requireWorkflowActionHandler(done) {
            return new Promise(function (resolve, reject) {                
                console.log('--- workflow action handler ---');
                jQuery('#workflow-links > .aui-buttons > #workflow-text').after(
                    '<a class="aui-button workflow-view-toggle" id="workflow-action-handler" href="#workflow-view-action-handler" data-mode="action-handler" resolved="">Action</a>');
            });
        }
    };

});

AJS.namespace("JIRA.WorkflowActionHandler.require", null, require("workflow-action-handler/application-loader").load);

AJS.toInit(function(){
	JIRA.WorkflowActionHandler.require();
});


