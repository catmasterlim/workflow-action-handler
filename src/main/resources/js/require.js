define("workflow-action-handler/application-loader", [
    "wrm/require",
    "require"
], function(
    wrmRequire,
    require
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
        load: function requireWorkflow(done) {
            
            return new Promise(function (resolve, reject) {

                console.log('------ log workflow 1 ');
                
            });
        }
    };

});

AJS.namespace("JIRA.WorkflowActionHandler.require", null, require("workflow-action-handler/application-loader").load);