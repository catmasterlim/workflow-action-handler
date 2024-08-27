
define('jira-workflow-action-handler/search-view', [
    "jquery",
    "jira-workflow-action-handler/templates",
    "jira-workflow-action-handler/variables",
    "jira-workflow-action-handler/utils",
    "jira-workflow-action-handler/search-options",
], function (
    jQuery,
    Templates,
    Variables,
    Utils,
    SearchOptionClass
) {

    class SearchViewClass {
        constructor(){
            this.doc = jQuery(document);
        }

        init(){
            // console.log(('---> SearchViewClass : init');
            this._setupSearchOption();
        }

        //
        _getInstSearchOptionSelected(){
            if(Variables.searchOptionSelected == undefined){
                Variables.searchOptionSelected = new Map();
            }
            return Variables.searchOptionSelected;
        }
        _isFilteredAction(trAction){
            let optionMap = this._getInstSearchOptionSelected();
//            let filterNames = ['action-name', 'action-type', 'action-class-type', 'action-class', 'action-class-simple', 'transition-id'];
            let filterNames = optionMap.keys();
            for(let filterName of filterNames){
                let val = jQuery(trAction).find('#'+filterName).attr('value');
                let option = optionMap.get(filterName);
                if(  option instanceof SearchOptionClass && option.isFiltered(val)) {
                    return true;
                }

            }
            return false;
        }

        _changeShowActionBySearchOption(){
             let actionList = jQuery('#container-workflow-action-handler-actions tbody tr');
             let countShowAction = 0;
             let countHideAction = 0;
             for(let trAction of actionList ){
                if(this._isFilteredAction(trAction)){
                    jQuery(trAction).addClass('hidden');
                    countHideAction++;
                }else{
                    jQuery(trAction).removeClass('hidden');
                    countShowAction++;
                }
             }

             // results-count-text
             let htmlActionCount = Templates.actionResultCount({
                     actionCount : countShowAction,
                     transitionCount : 0,
             });
             let containerActionCount = jQuery('.results-count-text');
             containerActionCount.empty();
             containerActionCount.append(htmlActionCount);

        }
        //  singletone - SearchOptionClass
        getInstSearchOptionClass(optionId, optionLabel, useFindInput, items, selectedItems, forceRefreshItems=false){

            let searchOptionClass = this._getInstSearchOptionSelected().get(optionId);
            if( searchOptionClass == undefined){
                searchOptionClass = new SearchOptionClass(optionId, optionLabel, useFindInput, items, selectedItems);
                searchOptionClass
                this._getInstSearchOptionSelected().set(optionId, searchOptionClass);
            }
            if(forceRefreshItems){
                searchOptionClass.items = items;
                searchOptionClass.removeSelectedNotIn();
            }
            return searchOptionClass;
        }



        _showActionList(){

            // action list
            let htmlActionList = Templates.actionList({
                title: "Action List",
                workflowMode : Variables.searchResult.workflowMode,
                atl_token : Utils.getDraftToken(),
                workflowName : Variables.searchResult.name,
                actions : Variables.searchResult.actions,
                maps : Variables.searchResult.maps
            });
            let containerActionList = jQuery('#container-workflow-action-handler-actions');
            containerActionList.empty();
            containerActionList.append(htmlActionList);

            // sorted table
            AJS.tablessortable.setTableSortable(AJS.$(".aui-table-sortable"));

            // reg event - delete
            this._regDeleteAction(jQuery('#container-workflow-action-handler-actions .criteria-condition-delete')) ;
            this._regDeleteAction(jQuery('#container-workflow-action-handler-actions .criteria-validator-delete')) ;
            this._regDeleteAction(jQuery('#container-workflow-action-handler-actions .criteria-post-function-delete')) ;

            //
            this._changeShowActionBySearchOption();
        }

        _regDeleteAction(btnDelete){
            btnDelete.on('click', e => {
                            e.preventDefault();
                            let linkNode = e.target;
                            let link = linkNode.getAttribute('href');
                            if(link == undefined){
                                linkNode = e.target.parentNode;
                                link = linkNode.getAttribute('href');
                            }

                            let workflowBaseUrl =  AJS.contextPath() + '/secure/admin/workflows/';

                            jQuery.ajax({
                                url : workflowBaseUrl +  link
                                , method : "GET"
                                , success : function(result, textStatus, jqXHR) {
                                    jQuery(linkNode).closest('.workflow-action-handler-item').remove();
                                    Utils.searchAction();
                                }
                                , error: function(jqXHR, textStatus, error) {
                                    AJS.messages.error("#workflow-action-handler-message", {
                                        id: 'js-message-example',
                                        title: 'fail',
                                        body: '<p>'+ error + '</p>'
                                    });
                                  }
                            });

                        });
        }

        searchResultProcess(){

            // search options
            this._setupSearchOption();

            //
            this._showActionList();

        }

        _getWorkflowData() {
                return {
                    isDraft: !!jQuery(".status-draft").length,
                    workflowMode : !!jQuery(".status-draft").length ? "draft" : "live",
                    isEditable: !!jQuery("#edit-workflow-trigger").length,
                    isInactive: !!jQuery(".status-inactive").length,
                    name: jQuery(".workflow-name").text(),
                    project: jQuery("#workflow-designer-project").val()
                };
        }



        async search(){

            let workflowData = this._getWorkflowData();
            // console.log(('--> workflowData : ', workflowData);
            let data = {
                "workflowMode" : workflowData.isDraft ? "draft" : "live"
                , "workflowName" : workflowData.name
                , "includedFiltered" : false
            };

            return  new Promise((resolve, reject) => {
                jQuery.ajax({
                    url : AJS.contextPath() +'/rest/workflowactionhandler/1.0/actions.json'
                    , method : "GET"
                    , dataType: 'json'
                    , data : jQuery.param(data)
                    , success : function(result, textStatus, jqXHR) {
                        resolve(result, textStatus, jqXHR);
                    }
                    , error: function(jqXHR, textStatus, error) {
                        console.error("error occurred");
                        reject(error);
                      }
                });
            }).then( (result) => {
                // console.log(('--> search then ok ');
                 Variables.searchResult = result;
                this.searchResultProcess();
            })
            ;

        }

      // options selected (checkItems)
      _setupSearchOption(){

            if(AJS.$('#container-workflow-action-handler-searchbar').length != 1 ){
                // console.log(('---> _setupSearchOption : not exists container-workflow-action-handler-searchbar');
                return;
            }

            let searchButton = AJS.$('#workflow-action-handler-search-button');
            if(searchButton.length != 1){
                console.error('---> _setupSearchOption : searchButton - not exists #workflow-action-handler-search-button');
            }

            searchButton.off('click');
            searchButton.on('click', e => {
                e.preventDefault();
                // console.log(('---> search actions');
                var that = e.target;
                if (!that.isBusy()) {
                    that.busy();
                    this.search().finally( () => that.idle() );
                }
            });

            if( Variables.searchResult == undefined ){
                Variables.searchResult = {
                        "isDraft" : false
                        , "name" : ""
                        , "actions" : []
                        , "maps" : {
                            "transitionMap" : {}
                            , "statusMap" : {}
                            , "statusCategoryMap" : {}
                        }
                    };
            }

            // console.log(('Variables.searchResult : ', Variables.searchResult);

            let searchOptionContainer = AJS.$('#container-workflow-action-handler-searchbar ul');
            searchOptionContainer.find('.search-option').empty();
            // reverse ( prepend ) - 아래 순서의 역순으로 option 등록
            {
                let optionId = 'transition-id';
                let items = {}
                let transitionMap = Variables.searchResult.maps.transitionMap;
                for(let a of Variables.searchResult.actions){
                    let transition = transitionMap[a.transitionId];
                    items[a.transitionId] = transition['name'] + '(' + a.transitionId + ')';
                }
                let searchOptionClass = this.getInstSearchOptionClass(optionId, 'Transition', true, items, {}, true);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent(this._changeShowActionBySearchOption.bind(this));
            }
            //
            {
                let optionId = 'action-plugin';
                let items = {};
                let itemsChecked = {};
                for(let a of Variables.searchResult.actions){
                  items[a.plugin.key] = a.plugin.name;
                }
                let searchOptionClass = this.getInstSearchOptionClass(optionId, 'Plugin', true, items, itemsChecked, true);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent(this._changeShowActionBySearchOption.bind(this));
            }
            //
            {
                let optionId = 'action-type';
                let items = {'Validator' : 'Validator','Condition' : 'Condition','PostFunction' : 'PostFunction', };
//                let itemsChecked = {'Condition': Condition,};
                let itemsChecked = {};
                let searchOptionClass = this.getInstSearchOptionClass(optionId, 'ActionType', false, items, itemsChecked);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent(this._changeShowActionBySearchOption.bind(this));
            }
            //
            {
                let items = {}
                let itemsChecked = {}
                for(let a of Variables.searchResult.actions){
                  items[a.name] = a.name;
                }
                let searchOptionClass = this.getInstSearchOptionClass('action-name', 'ActionName', true, items, itemsChecked, true);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent(this._changeShowActionBySearchOption.bind(this));
            }
      }
    }


//  ?.
  //--------------- end search options ----------- ---

  // console.log(('----> jira-workflow-action-handler/search-view');
  AJS.namespace("JIRA.WorkflowActionHandler.SearchView");

  let app = new SearchViewClass();
  return app;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchView", null, require("jira-workflow-action-handler/search-view"));

