define('jira-workflow-action-handler/search-view-dialog', [
    "jquery",
    "jira-workflow-action-handler/Application", 
    "jira-workflow-action-handler/templates",
    "jira-workflow-action-handler/variables"
], function (
    jQuery,
    Application,
    Templates,
    Variables
) {

    class SearchOptionClass {
        constructor(optionId, optionLabel, useFindInput, items, selectedItems ){
            this.optionId = optionId;
            this.optionLabel = optionLabel;
            this.useFindInput = useFindInput;
            this.items = items == undefined ?  {} : items;
            this.selectedItems = selectedItems == undefined ?  {} : selectedItems;
        }

        getHtml(){
            let html = Templates.searchOption({
                    optionId : this.optionId,
                    optionLabel : this.optionLabel,
                    useFindInput : this.useFindInput,
                    items : this.items,
                    selectedItems : this.selectedItems
                });
            return html;
        }

        register(){

            AJS.$(document).off('change', '#'+this.optionId+'-dropdown');
            AJS.$(document).on('change', '#'+this.optionId+'-dropdown', e => {
                let isChecked = e.target.hasAttribute('checked');
                let val = e.target.getAttribute('value');

                console.log('isChecked :',  isChecked);
                console.log('val :',val);

                if (isChecked) {
                    this.selectedItems[val] = true;
                } else {
                    delete this.selectedItems[val];
                }

                console.log('optionId', this.optionId, 'selectedItems  : ', this.selectedItems);

                let selectedOptionText = this.optionLabel;
                if(this.selectedItems.size > 0){
                    selectedOptionText = truncateString(Array.from(this.selectedItems).join(','), 15);
                }

                //
                console.log('selectedOptionText : ', selectedOptionText);
                jQuery('#'+this.optionId+'-button').text(selectedOptionText);
            });
            if(this.useFindInput){
                AJS.$(document).off('input', '#searcher-'+this.optionId+'-input');
                AJS.$(document).on('input', '#searcher-'+this.optionId+'-input', e => {
                    let val = e.target.value
                    console.log( 'find value ', val);
                    let items = AJS.$('#action-name-dropdown  aui-item-checkbox')

                    for(let item of items){
                        console.log('item text : ', item.textContent);
                        if(val=="" || item.textContent.includes(val) ){
                          item.style.visibility = "visible";
                        }else {
                          item.style.visibility = "hidden";
                        }
                    }
                });
            }
        }

        _eventSearchOption_find(e){
              let val = e.target.value
              console.log( 'find value ', val);

              let items = AJS.$('#action-name-dropdown  aui-item-checkbox')

              for(let item of items){
                console.log('item text : ', item.textContent);
                if(val=="" || item.textContent.includes(val) ){
                  item.style.visibility = "visible";
                }else {
                  item.style.visibility = "hidden";
                }
              }

            }
    }
    var searchOptionActionTypes = new Set([]);
    var searchOptionActionClassTypes = new Set([]);
    var searchOptionTransitionIds = new Set([]);
    var searchOptionTransitionNames = new Set([]);

    Variables.searchResult = {
        "isDraft" : false
        , "name" : ""
        , "actions" : []
    };

    function _arrayFrom(items, defaultVal){
        if( items === undefined || !items ){
            return defaultVal;
        }

        return Array.from(Object.keys(items));
    }

    function _getSearchActionOption() {

        let containerOptions = jQuery("#workflow-action-handler-dialog");
        let optionMap = JIRA.WorkflowActionHandler.Variables.searchOptionSelected;
        return {
            actionName: _arrayFrom(optionMap?.get('action-name')?.selectedItems, [])
            ,actionType: _arrayFrom(optionMap?.get('action-type')?.selectedItems, [])
            ,actionClassType: _arrayFrom(optionMap?.get('action-class-type')?.selectedItems, [])
            ,actionClass: []
            ,transitionId: Array.from(searchOptionTransitionIds)
            ,transitionName: Array.from(searchOptionTransitionNames)
        };
    }

    //
    function truncateString(str, maxLength) {
        if (str.length > maxLength) {
            return [str.slice(0, maxLength - 3), '...'].join('');
        }
        return str;
    }

    function searchResultProcess(){

//        let searchOption = _getSearchActionOption();

        // search option action name
//        let htmlActionNameList = Templates.searchOptionActionName({
//            isDraft : searchResult.isDraft,
//            workflowName : searchResult.name,
//            actions : searchResult.actions,
//            searchOption : searchOption
//        });
//        let nameItems = AJS.$('#action-name-dropdown .aui-list-truncate');
//        nameItems.empty();
//        nameItems.append(htmlActionNameList);

        // search options
        _setupSearchOption();

        // action list
        let htmlActionList = Templates.actionList({
            title: "Action List",
            isDraft : Variables.searchResult.isDraft,
            workflowName : Variables.searchResult.name,
            actions : Variables.searchResult.actions
        });
        let containerActionList = jQuery('#container-workflow-action-handler-actions');
        containerActionList.empty();
        containerActionList.append(htmlActionList);

        // sorted table
        AJS.tablessortable.setTableSortable(AJS.$(".aui-table-sortable"));
    }
    
    async function search(){
        let classThis = JIRA.WorkflowActionHandler.Variables.Application;
        let searchOption = _getSearchActionOption();
        console.log('----> search options', searchOption);

        let data = {
            "isDraft" : classThis._workflowData.isDraft
            , "workflowName" : classThis._workflowData.name
            , "actionName" : searchOption.actionName
            , "actionType" : searchOption.actionType
            , "actionClassType" : searchOption.actionClassType
            , "transitionId" : searchOption.transitionId
            , "transitionName" : searchOption.transitionName
        };
        console.log(classThis._workflowData);
        console.log(data);

        return  new Promise((resolve, reject) => {
            jQuery.ajax({
                url : AJS.contextPath() +'/rest/workflowactionhandler/1.0/actions.json'
                , method : "GET"
                , dataType: 'json'
                , data : jQuery.param(data)
                , success: function(result, textStatus, jqXHR) {
                    Variables.searchResult = result;
                    searchResultProcess();
                    resolve({result, textStatus, jqXHR});
                  }
                , error: function(jqXHR, textStatus, error) {
                    console.error("error occurred");
                    reject(error); 
                  }
            });
        });
        
    }

    function _eventSearch(e){
        e.preventDefault();
        
        var that = this;

        console.log('---> search actions');
        console.log(that);

        if (!that.isBusy()) {
            that.busy();
            search().finally( () => that.idle() );    
        }
    }

    //   this._dialog._btn_search = jQuery('#workflow-action-handler-search-button');
    //   this._dialog._btn_search.on('click', this._eventSearch);
    console.log("---> _eventSearch", _eventSearch);
    AJS.$(document).on('click', '#workflow-action-handler-search-button', _eventSearch);



  // options selected (checkItems)
  function _setupSearchOption(){
      if(Variables.searchOptionSelected == undefined){
          Variables.searchOptionSelected = new Map();
      }

      // Variables.searchResult

      let searchOptionContainer = AJS.$('#container-workflow-action-handler-searchbar ul');
      searchOptionContainer.find('.search-option').empty();
      // reverse ( prepend )
      //
    {
          let optionId = 'action-class-type';
          let searchOptionClass = Variables.searchOptionSelected.get(optionId);
          if( searchOptionClass == undefined){
              let items = {
                                  'Default' : 'Default',
                                  'JiraBase' : 'JiraBase',
                                  'Custom' : 'Custom',
                          };
              let itemsChecked = {
                'JiraBase': true,
                'Custom': true,
              };

              searchOptionClass = new SearchOptionClass(optionId, 'ActionClassType', false, items, itemsChecked);
              Variables.searchOptionSelected.set(optionId, searchOptionClass);
          }
          let html = searchOptionClass.getHtml();
          searchOptionContainer.prepend(html);
          searchOptionClass.register();
      }
      //
      {
            let optionId = 'action-type';
            let searchOptionClass = Variables.searchOptionSelected.get(optionId);
            if( searchOptionClass == undefined){
                let items = {
                                    'Validator' : 'Validator',
                                    'Condition' : 'Condition',
                                    'PostFunction' : 'PostFunction',
                            };
                let itemsChecked = {
                  'Validator': true,
                };

                searchOptionClass = new SearchOptionClass(optionId, 'ActionType', false, items, itemsChecked);
                Variables.searchOptionSelected.set(optionId, searchOptionClass);
            }
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.register();
        }
      //
      {
          let optionId = 'action-name';
          let searchOptionClass = Variables.searchOptionSelected.get(optionId);
          if( searchOptionClass == undefined){
              let items = {};
              let itemsChecked = {};

              searchOptionClass = new SearchOptionClass(optionId, 'ActionName', true, items, itemsChecked);
              Variables.searchOptionSelected.set(optionId, searchOptionClass);
          }
          let html = searchOptionClass.getHtml();
          searchOptionContainer.prepend(html);
          searchOptionClass.register();
      }
  }

  //
  _setupSearchOption();

  //--------------- end search options ----------- ---
  AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog");
  return JIRA.WorkflowActionHandler.SearchViewDialog;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog", null, require("jira-workflow-action-handler/search-view-dialog"));
