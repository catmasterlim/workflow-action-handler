define("jira-workflow-action-handler/Application", [
    "wrm/require",
    "wrm/data",
    "jquery",
    "require",
    "jira-workflow-action-handler/templates"
], function(
    wrmRequire,
    WRMData,
    jQuery,
    require,
    Templates
) {
    class ApplicationWorkflowActionHandler {
        constructor(){
            this.doc = jQuery(document);
//            this.instance = this;
        }
        //
        execute(){
            this._setupPage();
           /* //
            console.log("this._workflowView.length : " + this._workflowView.length);
            if (this._workflowView.length > 0) {

            }*/
        }

        //----------------------------------------------------------------------
        // method
        //----------------------------------------------------------------------
        _getWorkflowData() {
            return {
                isDraft: !!jQuery(".status-draft").length,
                isEditable: !!jQuery("#edit-workflow-trigger").length,
                isInactive: !!jQuery(".status-inactive").length,
                name: jQuery(".workflow-name").text(),
                project: jQuery("#workflow-designer-project").val()
            };
        }

        _getSearchView() {
            console.log(  " _getSearchView - _workflowData : " + this._workflowData);
            return Templates.searchDialog({
                title: "Action Search Dialog",
                isDraft : this._workflowData.isDraft,
                workflowName : this._workflowData.name,
                actions : []
            });
        }

        _actionLinkOnClick(e){
            e.preventDefault();
            AJS.dialog2(this._workflowView).show();
        }
        _setupActionLink(){
            let actionLinkEl = jQuery("#jira-workflow-action-handler");
            // button
            let hasLinkButton = !!actionLinkEl.length;
            console.log('--->  hasLinkButton : ' + hasLinkButton);
            if( hasLinkButton ){
                return;
            }

            // link buttons ( Digram | Text ) - Text
//            let textButton = jQuery('#workflow-links > .aui-buttons > #workflow-text');
            let el = jQuery('#workflow-links');
            console.log('--->  target : ' + el.length);
            if(el.length > 0){
                el.append(
                    '<a class="aui-button" id="jira-workflow-action-handler" resolved="">Action Search</a>'
                );
            }

            actionLinkEl = jQuery("#jira-workflow-action-handler");
            actionLinkEl.on('click', this._actionLinkOnClick.bind(this));
        }

        _dialogSubmit(e){
            e.preventDefault();
            AJS.dialog2(this._dialog._id).hide();
        }

        _setupDialog(){
            this._dialog = {};
            this._dialog._id = jQuery("#workflow-action-handler-dialog");

            let has = !!this._dialog._id.length;
            if( has ){
                return this._dialog._id;
            }

            let insertEl = jQuery('#main');
            if( insertEl && insertEl.length > 0 ){
                let tt = this._getSearchView();
                insertEl.after(tt);
            }

            this._dialog._id = jQuery("#workflow-action-handler-dialog");
            this._dialog._submit = jQuery('#workflow-action-handler-dialog-submit-button');

            AJS.$("#search-item-action-name").auiSelect2();
            AJS.$("#search-item-action-type").auiSelect2();

            this._dialog._submit.on('click', this._dialogSubmit.bind(this));


            //
            if (this._workflowData.isEditable) {
            }else{
            }

            this._searchActions();

            return this._dialog._id;
        }

        _searchActions(){
            console.log('---> search actions');

            let data = {
                "isDraft" : this._workflowData.isDraft
                , "workflowName" : this._workflowData.name
                , "actionTypes" : ["Validator"]
            };
            console.log(this._workflowData);
            console.log(data);

            return jQuery.ajax({
                url : AJS.contextPath() +'/rest/workflowactionhandler/1.0/actions.json'
                , method : "GET"
                , dataType: 'json'
                , data : jQuery.param(data)
            });
            // }).done(function(data){
            //     console.log('--- done -----');
            //     console.log(data);
            // }).fail(function(xhr, textStatus, errorThrown){
            //     console.log('--- fail -----');
            //     console.log(errorThrown);
            // });
            
        }

        _setupPage() {

            console.log('---> setup');

            this._workflowData = this._getWorkflowData();
            this._workflowLinkContainer = jQuery("#workflow-links");
            this._workflowActionLink = this._setupActionLink();
            this._workflowView = this._setupDialog();
        }
    }

    let app = new ApplicationWorkflowActionHandler();
    return app;
});




require([
    "jira-workflow-action-handler/Application",
    "jquery"
], function(
    Application,
    jQuery
) {
    jQuery(function () {
        Application.execute();
    });

});

AJS.namespace("JIRA.WorkflowActionHandler.Application", null, require("jira-workflow-action-handler/Application"));
