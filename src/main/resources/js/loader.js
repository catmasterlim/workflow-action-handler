
define("jira-workflow-action-handler/loader", [
    "wrm/require",
    "jquery",
    "require"
], function(
    wrmRequire,
    jQuery,
    require,
) {
    return {
        /**
         * Load the resources required , then execute a callback.
         *
         * This method can be safely called multiple times, the resources will only be loaded once.
         *
         *   application constructor.
         * @returns a jQuery promise that is resolved on success, or rejected on failure.
         */
        load: function requireWRM(done) {
            return new Promise(function (resolve, reject) {
                wrmRequire(["wrc!dev.jiraWorkflowActionHandler"], function () {
                    done && done(require("jira-workflow-action-handler/loader"));
                }).done(resolve).fail(reject);
            });
        }
    };
});

AJS.namespace("JIRA.WorkflowActionHandler.loader", null, require("jira-workflow-action-handler/loader").load);



