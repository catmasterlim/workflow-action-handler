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
    Templates
) {
    var Application = function() {   
        
        var doc = jQuery(document);
        var instance = this;


        _initWorkflowActionHandler = function() {
            var self = this;           
            console.log('---> _initWorkflowActionHandler');

            AJS.$("#search-item-action-name").auiSelect2();
            AJS.$("#search-item-action-type").auiSelect2();

            AJS.$("#dialog-submit-button").on('click', function (e) {
                e.preventDefault();
                AJS.dialog2("#demo-dialog").hide();
            });

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

        _getHtmlString = function() {
            // console.log(this._workflowData);
            return Templates.test({
                workflowMode : instance._workflowData.isDraft,
                workflowName : instance._workflowData.name
            });     
        };

        _prepare = function() {

            //
            instance._workflowData =  _getWorkflowData();
            instance._workflowLinkContainer = jQuery("#workflow-links");
            
            // button
            var hasButton = !!jQuery("#workflow-action-handler").length;
            console.log('--->  hasButton : ' + hasButton);
            if( !hasButton ){                
                // link buttons ( Digram | Text ) - Text
                let textButton = jQuery('#workflow-links > .aui-buttons > #workflow-text');
                console.log('--->  textButton : ' + textButton);
                if(textButton.length > 0){
                    textButton.after(
                        '<a class="aui-button" id="workflow-action-handler" href="#workflow-view-action-handler" resolved="">Action</a>'
                    );
                }

                //
                let workflow_view_text = jQuery('#workflow-view-text');
                console.log('--->  workflow_view_text : ' + workflow_view_text);
                if(workflow_view_text.length > 0){
                    let tt = _getHtmlString();
                    console.log('--->  test2 : ' + tt);    
                    workflow_view_text.after(tt);
                }                
            }

            instance._workflowView = jQuery("#workflow-view-action-handler");

    
        };

        _setupPage = function() {
            
            console.log('---> setup');
            //
            if (!instance._workflowView.hasClass("hidden")) {
                
                _initWorkflowActionHandler();
            } else {
                _initWorkflowActionHandler();
                console.log('---> set click');
                doc.on("click", "#workflow-action-handler", function(e) {
                    e.preventDefault();
                    console.log('---> click');                    
                    // doc.off(e, "#workflow-action-handler");
                    instance._workflowView.show();
                    AJS.dialog2("#demo-dialog").show();
                });
            }
    
            // 
            if (instance._workflowData.isEditable) {
            }else{                
            }

          };


        //
        _prepare();



        //
        console.log("this._workflowView.length : " + instance._workflowView.length);
        if (instance._workflowView.length > 0) {
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
