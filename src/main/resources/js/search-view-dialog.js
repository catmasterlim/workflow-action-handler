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

    // options selected
    var searchOptionSelected = {
    }
    class SearchOptionClass {
        constructor(optionId, optionLabel, useFindInput, items, checkedItems ){
            this.optionId = optionId;
            this.optionLabel = optionLabel;
            this.useFindInput = useFindInput;
            this.items = items;
            this.checkedItems = checkedItems;

            this.selected = new Set([]);
        }

        getHtml(){
            let html = Templates.searchOption({
                    optionId : this.optionId,
                    optionLabel : this.optionLabel,
                    useFindInput : this.useFindInput,
                    items : this.items,
                    checkedItems : this.checkedItems
                });
            return html;
        }

        register(){
            AJS.$(document).on('change', '#'+this.optionId+'-dropdown', e => {
                let isChecked = e.target.hasAttribute('checked');
                let val = e.target.getAttribute('value');

                console.log('isChecked :',  isChecked);
                console.log('val :',val);

                if(searchOptionSelected)

                if (isChecked) {
                    this.selected.add(val);
                } else {
                    this.selected.delete(val);
                }

                console.log('selected  : ', this.selected);

                let selectedOptionText = this.optionLabel;
                if(this.selected.size > 0){
                    selectedOptionText = truncateString(Array.from(this.selected).join(','), 15);
                }

                //
                console.log('text : ', selectedOptionText);
                jQuery('#'+this.optionId+'-button').text(selectedOptionText);
            });
            if(this.useFindInput){
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

    var searchResult = {
        "isDraft" : false
        , "name" : ""
        , "actions" : []
    };


    function _getSearchActionOption() {

        let containerOptions = jQuery("#workflow-action-handler-dialog");

        return {
            actionName: Array.from(actionNameOptionClass.selected)
            ,actionType: Array.from(searchOptionActionTypes)
            ,actionClassType: Array.from(searchOptionActionClassTypes)
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

        let searchOption = _getSearchActionOption();

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

        let searchOptionContainer = AJS.$('#container-workflow-action-handler-searchbar .search-option');
        searchOptionContainer.empty();

        //---------------------------------------------------------
        //--------------------- < action name > -------------------

        let names = {'Validator' : 'Validator', 'Condition' : 'Condition', 'PostFunction' : 'PostFunction'}
        let namesChecked = {'Validator' : true }
        var actionNameOptionClass = new SearchOptionClass('action-name', 'ActionName', true, names, namesChecked);
        let html = actionNameOptionClass.getHtml();
        console.log('----> actionNameOptionClass html : ', html)
        searchOptionContainer.append(html);
        actionNameOptionClass.register();

        // action list
        let htmlActionList = Templates.actionList({
            title: "Action List",
            isDraft : searchResult.isDraft,
            workflowName : searchResult.name,
            actions : searchResult.actions
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
                    searchResult = result;
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




    //--------------------------------------------------------- 
    //--------------------- < action type > -------------------
    function _eventSearchOption_atype(e){  
        let isChecked = e.target.hasAttribute('checked');
        let val = e.target.getAttribute('value')
        if (isChecked) {
            searchOptionActionTypes.add(val);
          } else {
            searchOptionActionTypes.delete(val);
          }
        
        console.log('searchOptionActionTypes : ', searchOptionActionTypes);
  
        // 
        let text = "Type : All";
        if(searchOptionActionTypes.size > 0){
          text = truncateString(Array.from(searchOptionActionTypes).join(','), 15);
        } 
        
        // 
        console.log('text : ', text);
        jQuery('#action-type-button').text(text);
        
      }
      
    AJS.$(document).on('change', '#action-type-dropdown', _eventSearchOption_atype);
      
      
    function _eventSearchOption_atype_find(e){
    let val = e.target.value
    console.log( 'find value ', val);

    let items = AJS.$('#action-type-dropdown  aui-item-checkbox')

    for(let item of items){
      console.log('item text : ', item.textContent);
      if(val=="" || item.textContent.includes(val) ){
        item.style.visibility = "visible";
      }else {
        item.style.visibility = "hidden";
      }
    }

  }
    AJS.$(document).on('input', '#searcher-atype-input', _eventSearchOption_atype_find);


  //---------------------------------------------------------
  //--------------------- < action class type > -------------------
  function _eventSearchOption_aclasstype(e){
      let isChecked = e.target.hasAttribute('checked');
      let val = e.target.getAttribute('value')
      if (isChecked) {
          searchOptionActionClassTypes.add(val);
        } else {
          searchOptionActionClassTypes.delete(val);
        }

      console.log('searchOptionActionClassTypes : ', searchOptionActionClassTypes);

      //
      let text = "ClassType : All";
      if(searchOptionActionClassTypes.size > 0){
        text = truncateString(Array.from(searchOptionActionClassTypes).join(','), 15);
      }

      //
      console.log('text : ', text);
      jQuery('#action-class-type-button').text(text);

    }
  AJS.$(document).on('change', '#action-class-type-dropdown', _eventSearchOption_aclasstype);

  function _eventSearchOption_aclasstype_find(e){
      let val = e.target.value
      console.log( 'find value ', val);

      let items = AJS.$('#action-class-type-dropdown  aui-item-checkbox')

      for(let item of items){
        console.log('item text : ', item.textContent);
        if(val=="" || item.textContent.includes(val) ){
          item.style.visibility = "visible";
        }else {
          item.style.visibility = "hidden";
        }
      }

    }
  AJS.$(document).on('input', '#searcher-aclasstype-input', _eventSearchOption_aclasstype_find);


  //---------------------------------------------------------
  //--------------------- < transition id > -------------------
  function _eventSearchOption_transitionId(e){
    let isChecked = e.target.hasAttribute('checked');
    let val = e.target.getAttribute('value')
    if (isChecked) {
        searchOptionTransitionIds.add(val);
      } else {
        searchOptionTransitionIds.delete(val);
      }

    console.log('searchOptionTransitionIds : ', searchOptionTransitionIds);

    //
    let text = "Transition : All";
    if(searchOptionTransitionIds.size > 0){
      text = truncateString(Array.from(searchOptionTransitionIds).join(','), 15);
    }

    //
    console.log('text : ', text);
    jQuery('#transition-id-button').text(text);

  }
  AJS.$(document).on('change', '#transition-id-dropdown', _eventSearchOption_transitionId);

  function _eventSearchOption_transitionId_find(e){
    let val = e.target.value
    console.log( 'find value ', val);

    let items = AJS.$('#transition-id-dropdown  aui-item-checkbox')

    for(let item of items){
      console.log('item text : ', item.textContent);
      if(val=="" || item.textContent.includes(val) ){
        item.style.visibility = "visible";
      }else {
        item.style.visibility = "hidden";
      }
    }
  }
  AJS.$(document).on('input', '#searcher-transition-id-input', _eventSearchOption_transitionId_find);


  //--------------- end search options ----------- ---
  AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog");
  return JIRA.WorkflowActionHandler.SearchViewDialog;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog", null, require("jira-workflow-action-handler/search-view-dialog"));
