define("jira-workflow-action-handler/Application", [
    "wrm/require",
    "wrm/data",
    "jquery",
    "require",
    "jira-workflow-action-handler/templates",
    "jira-workflow-action-handler/variables",
    "jira-workflow-action-handler/search-view",
    "jira-workflow-action-handler/utils"
], function(
    wrmRequire,
    WRMData,
    jQuery,
    require,
    Templates,
    Variables,
    SearchView,
    Utils
) {

    class ApplicationWorkflowActionHandler {
        constructor(){
            this.doc = jQuery(document);
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
                workflowMode : this._workflowData.isDraft ? "draft" : "live",
                workflowName : this._workflowData.name,
                actions : []
            });
        }

        _eventShow(e){
            e.preventDefault();
            let classThis = JIRA.WorkflowActionHandler.Variables.Application;
            Utils.searchAction();
            AJS.dialog2(classThis._workflowView).show();
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
            let el = jQuery('#workflow-links');
            console.log('--->  target : ' + el.length);
            if(el.length > 0){
                el.append(
                    '<a class="aui-button" id="jira-workflow-action-handler" resolved="">Action Search</a>'
                );
            }

            actionLinkEl = jQuery("#jira-workflow-action-handler");
            actionLinkEl.on('click', this._eventShow.bind(this));
        }

        _setupSearchOption(){
            // action-name-button
            // action-name-dropdown

            // action-type-button
            // action-type-dropdown
        }

        
        _eventHide(e){
            e.preventDefault();
            let classThis = JIRA.WorkflowActionHandler.Variables.Application;
            AJS.dialog2(classThis._dialog._id).hide();
        }
        _setupSearchView(){
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
                SearchView.init();
            }

            this._dialog._id = jQuery("#workflow-action-handler-dialog");
            this._dialog._btn_hide = jQuery('#workflow-action-handler-dialog-submit-button');
            // this._dialog._btn_search = jQuery('#workflow-action-handler-search-button');

//            AJS.$("#search-item-action-name").auiSelect2();
//            AJS.$("#search-item-action-type").auiSelect2();
            // AJS.$(this._dialog._btn_search).auiButton2();

            // this._dialog._btn_hide.on('click', this._eventHide.bind(this));
            // this._dialog._btn_search.on('click', this._eventSearch.bind(this));

            this._dialog._btn_hide.on('click', this._eventHide);
            // this._dialog._btn_search.on('click', this._eventSearch);
            
            

            //
            if (this._workflowData.isEditable) {
            }else{
            }

            // click
            // this._dialog._btn_search.trigger('click');

            return this._dialog._id;
        }

        


        _setupPage() {

            console.log('---> _setupPage');

            this._workflowData = this._getWorkflowData();
            this._workflowLinkContainer = jQuery("#workflow-links");
            this._workflowActionLink = this._setupActionLink();
            this._workflowView = this._setupSearchView();
        }
    }

    console.log('----> jira-workflow-action-handler/Application');
    AJS.namespace("JIRA.WorkflowActionHandler.Application");

    let app = new ApplicationWorkflowActionHandler();
    Variables.Application = app;
    return app;
});

AJS.namespace("JIRA.WorkflowActionHandler.Application", null, require("jira-workflow-action-handler/Application"));

AJS.toInit(function() {
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

});



