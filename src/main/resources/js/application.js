define("workflow-action-handler/application", [
    "wrm/require",
    "wrm/data",
    "jquery",
    "require",
    "workflow-action-handler/templates"
], function(
    wrmRequire,
    WRMData,
    jQuery,
    require,
    templates
) {
    var Application = function() {                

        _initWorkflowActionHandler = function() {
            var self = this;           
        };

        _getWorkflowData = function() {
            return {
                isDraft: !!jQuery(".status-draft").length,
                isEditable: !!jQuery("#edit-workflow-trigger").length,
                isInactive: !!jQuery(".status-inactive").length,
                name: jQuery(".workflow-name").text(),
                project: jQuery("#workflow-designer-project").val()
            };
        };

        _prepare = function() {
            // button
            var hasButton = !!jQuery("#workflow-action-handler-button").length;
            console.log('--->  hasButton : ' + hasButton);
            if( !hasButton ){                
                // link buttons ( Digram | Text ) - Text
                var textButton = jQuery('#workflow-links > .aui-buttons > #workflow-text');
                console.log('--->  textButton : ' + textButton);
                if(textButton.length > 0){
                    textButton.after(
                        '<a class="aui-button workflow-view-toggle" id="workflow-action-handler-button" href="#workflow-view-action-handler" data-mode="action-handler" resolved="">Action</a>'
                    );
                }
            }
    
        };

        _setupPage = function() {
            var doc = jQuery(document);
            var instance = this;
            
            //
            if (!this._workflowView.hasClass("hidden")) {
                this._initWorkflowActionHandler();
            } else {
                doc.on("click", "#workflow-action-handler-button", function(e) {
                    instance._initWorkflowActionHandler();
                    doc.off(e, "#workflow-action-handler-button");
                });
            }
    
            // 
            if (this._workflowData.isEditable) {
            }else{                
            }
    
            //
          };

        this._workflowData =  _getWorkflowData();
        this._workflowLinkContainer = jQuery("#workflow-links");
        this._workflowView = jQuery("#workflow-view-action-handler");

        //
        _prepare();

        //
        if (this._workflowView.length > 0) {
            _setupPage();
        }
    };


    return Application; 
    

});

require([
    "workflow-action-handler/application",
    "jquery"
], function(
    Application,
    jQuery
) {
    jQuery(function () {
        new Application();
    });

});

AJS.namespace("JIRA.WorkflowActionHandler.Application", null, require("workflow-action-handler/application"));
