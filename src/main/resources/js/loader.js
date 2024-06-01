
define("workflow-action-handler/loader", [
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
         * Load the resources required for the workflow designer, then execute a callback.
         *
         * This method can be safely called multiple times, the resources will only be loaded once.
         *
         * @param {function} [done] A callback to execute after the resources have been loaded. It's given the workflow designer
         *   application constructor.
         * @returns a jQuery promise that is resolved on success, or rejected on failure.
         */
        load: function requireWRM(done) {
            return new Promise(function (resolve, reject) {
                wrmRequire(["wrc!dev.jira.workflowActionHandler"], function () {
                    done && done(require("workflow-action-handler/loader"));
                }).done(resolve).fail(reject);
            });
        }
    };
});

AJS.namespace("JIRA.WorkflowActionHandler.loader", null, require("workflow-action-handler/loader").load);



