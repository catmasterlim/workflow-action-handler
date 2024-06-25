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

    // options
    var searchOptionActionNames = new Set([]);
    var searchOptionActionTypes = new Set([]);


    function _getSearchActionOption() {

        let containerOptions = jQuery("#workflow-action-handler-dialog");

        return {
            actionName: Array.from(searchOptionActionNames)
            ,actionType: Array.from(searchOptionActionTypes)
            ,actionClass: []
            ,transitionId: []
        };

        // return {
        //     actionName: containerOptions.find(".search-option-action-name")
        //     ,actionType: containerOptions.find(".search-option-action-type")
        //     ,actionClass: containerOptions.find(".search-option-action-class")
        //     ,transitionId: containerOptions.find(".search-option-action-transition-id")
        // };
    }

    //
    function truncateString(str, maxLength) {
        if (str.length > maxLength) {
            return [str.slice(0, maxLength - 3), '...'].join('');
        }
        return str;
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
                    
                    // search option action name
                    let htmlActionNameList = Templates.searchOptionActionName({
                        isDraft : result.isDraft,
                        workflowName : result.name,
                        actions : result.actions
                    });
                    let nameItems = AJS.$('#action-name-dropdown aui-item-checkbox');
                    nameItems.empty();
                    nameItems.append(htmlActionNameList);

                    // action list
                    let htmlActionList = Templates.actionList({
                        title: "Action List",
                        isDraft : result.isDraft,
                        workflowName : result.name,
                        actions : result.actions
                    });
                    let containerActionList = jQuery('#container-workflow-action-handler-actions');
                    containerActionList.empty();
                    containerActionList.append(htmlActionList);

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

    //--------------------------------------------------------- 
    //--------------------- < action name > -------------------
   
    function _eventSearchOption_aname(e){  
      let isChecked = e.target.hasAttribute('checked');
      let val = e.target.getAttribute('value')
      if (isChecked) {
          searchOptionActionNames.add(val);
        } else {
          searchOptionActionNames.delete(val);
        }
      
      console.log('searchOptionActionNames : ', searchOptionActionNames);

      // 
      let text = "Action : All";
      if(searchOptionActionNames.size > 0){
        text = truncateString(Array.from(searchOptionActionNames).join(','), 15);
      } 
      
      // 
      console.log('text : ', text);
      jQuery('#action-name-button').text(text);
      
    }
    
    AJS.$(document).on('change', '#action-name-dropdown', _eventSearchOption_aname);
    
    
    function _eventSearchOption_aname_find(e){  
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
    AJS.$(document).on('input', '#searcher-aname-input', _eventSearchOption_aname_find);


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

    //   this._dialog._btn_search = jQuery('#workflow-action-handler-search-button');
    //   this._dialog._btn_search.on('click', this._eventSearch);
      AJS.$(document).on('click', '#workflow-action-handler-search-button', _eventSearch);

    //
    AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog");
    return JIRA.WorkflowActionHandler.SearchViewDialog;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog", null, require("jira-workflow-action-handler/search-view-dialog"));