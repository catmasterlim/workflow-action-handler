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
    //
    function getInstSearchOptionSelected(){
        if(Variables.searchOptionSelected == undefined){
            Variables.searchOptionSelected = new Map();
        }
        return Variables.searchOptionSelected;
    }
    function _isFilteredAction(trAction){
        let optionMap = getInstSearchOptionSelected();

        let filterNames = ['action-name', 'action-type', 'action-class-type', 'action-class', 'action-class-simple', 'transition-id', 'transition-name'];
        for(let filterName of filterNames){
            let val = jQuery(trAction).find('#'+filterName).attr('value');
            if( optionMap.get(filterName)?.isFiltered(val)) {
                console.log( '_isFilteredAction true => ',  ' filterName : ', filterName, ',val : ', val);
                return true;
            }
        }

//        console.log( '_isFilteredAction false => ',  ' filterNames : ', filterNames);
        return false;
    }
    function changeShowActionBySearchOption(){
         let actionList = jQuery('#container-workflow-action-handler-actions tbody tr');
         let countShowAction = 0;
         let countHideAction = 0;
         for(let trAction of actionList ){
//            console.log(' trAction _isFilteredAction : ', _isFilteredAction(trAction), ' trAction : ' , trAction);
            if(_isFilteredAction(trAction)){
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
    function getInstSearchOptionClass(optionId, optionLabel, useFindInput, items, selectedItems, forceRefreshItems=false){

        let searchOptionClass = getInstSearchOptionSelected().get(optionId);
        if( searchOptionClass == undefined){
            searchOptionClass = new SearchOptionClass(optionId, optionLabel, useFindInput, items, selectedItems);
            getInstSearchOptionSelected().set(optionId, searchOptionClass);
        }

        if(forceRefreshItems){
            searchOptionClass.items = items;
            searchOptionClass.removeSelectedNotIn();
        }

        return searchOptionClass;
    }

    class SearchOptionClass {
        constructor(optionId, optionLabel, useFindInput, items, selectedItems ){
            this.optionId = optionId;
            this.optionLabel = optionLabel;
            this.useFindInput = useFindInput;
            this.useShowSelectedOnTop = useFindInput;
            this.items = items == undefined ?  {} : items;
            this.selectedItems = selectedItems == undefined ?  {} : selectedItems;
        }

        getHtml(){
            let html = Templates.searchOption({
                    optionId : this.optionId,
                    optionLabel : this.optionLabel,
                    useFindInput : this.useFindInput,
                    useShowSelectedOnTop : this.useShowSelectedOnTop,
                    items : this.items,
                    selectedItems : this.selectedItems
                });
            return html;
        }

        //
        removeSelectedNotIn(){
            let keyItems = Object.keys(this.items);
            let keySelectedItems = Object.keys(this.selectedItems);

            let newSelected = {}
            for(let key of keySelectedItems){
                if(keyItems.includes(key)){
                    newSelected[key] = true;
                }
            }

            this.selectedItems = newSelected;
        }

        isFiltered(val){
            if( val == undefined ){
                console.error(' isFitlered val is undefined' );
                return false;
            }

            if(  Object.keys(this.selectedItems).length == 0 ){
                return false;
            }

            if( ! this.selectedItems[val] ) {
                return true;
            }

            return false;
        }
        isNotFiltered(val){
            return ! this.isFiltered(val);
        }

        regEvent(){
            let elDropdown = jQuery(`#${this.optionId}-dropdown`);
            elDropdown.off('change');
            elDropdown.on('change', e => {
                e.preventDefault();
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

                this._changeSelected(val, isChecked);
            });

            if(this.useShowSelectedOnTop){
                let elClear = jQuery(`#${this.optionId}-dropdown .selected-items .clear-all`);
                elClear.off('click');
                elClear.on('click', e => {
                    e.preventDefault();
                    let selected = jQuery(`#${this.optionId}-dropdown .selected-items aui-item-checkbox`);
                    for(let item of selected){
                        item.removeAttribute('checked');
                    }
                });
            }
            if(this.useFindInput){
                let elInput = jQuery('#searcher-'+this.optionId+'-input');

                elInput.off('input');
                elInput.on('input', e => {
                    e.preventDefault();
                    let val = e.target.value
                    console.log( 'find value ', val);
                    let elItems = jQuery('#'+this.optionId+'-dropdown .items aui-item-checkbox');
                    for(let item of elItems){
                        console.log('item text : ', item.textContent);
                        if(val=="" || item.textContent.includes(val) ){
                          jQuery(item).show();
                        }else {
                           jQuery(item).hide();
                        }
                    }
                });
            }
        }

        _changeSelected(val, isChecked){
            let hasSelected = Object.keys(this.selectedItems).length > 0;
            let selectedOptionText = this.optionLabel + " : All";
            if( hasSelected ){
                selectedOptionText = truncateString( _arrayFromValues(this.selectedItems).join(','), 15);
            }
            console.log('selectedOptionText : ', selectedOptionText);
            jQuery('#'+this.optionId+'-button').text(selectedOptionText);

            // selected to top
            let elContainerSelected = jQuery(`#${this.optionId}-dropdown .selected-items`);
            if(this.useShowSelectedOnTop && elContainerSelected.length > 0) {

                let elClear = jQuery(`#${this.optionId}-dropdown .selected-items .clear-all`);
                let elContainer = jQuery(`#${this.optionId}-dropdown .items`);
                let elSelected = jQuery(`#${this.optionId}-dropdown aui-item-checkbox[value="${val}"]`);

                if( hasSelected ){
                    elClear.show();
                } else {
                    elClear.hide();
                }

                if(isChecked) {
                    elContainerSelected.append(jQuery(elSelected).detach());
                } else {
                    elContainer.append(jQuery(elSelected).detach());
                }
            }

            //
            changeShowActionBySearchOption();

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


    function _arrayFromKeys(items, defaultVal){
        if( items === undefined || !items ){
            return defaultVal;
        }

        return Array.from(Object.keys(items));
    }
    function _arrayFromValues(items, defaultVal){
            if( items === undefined || !items ){
                return defaultVal;
            }

            return Array.from(Object.keys(items));
        }


    //
    function truncateString(str, maxLength) {
        if (str.length > maxLength) {
            return [str.slice(0, maxLength - 3), '...'].join('');
        }
        return str;
    }



    function _showActionList(){

        // action list
        let htmlActionList = Templates.actionList({
            title: "Action List",
            isDraft : Variables.searchResult.isDraft,
            workflowName : Variables.searchResult.name,
            actions : Variables.searchResult.actions,
            maps : Variables.searchResult.maps
        });
        let containerActionList = jQuery('#container-workflow-action-handler-actions');
        containerActionList.empty();
        containerActionList.append(htmlActionList);

        // sorted table
        AJS.tablessortable.setTableSortable(AJS.$(".aui-table-sortable"));

        //
        changeShowActionBySearchOption();
    }

    function searchResultProcess(){

        // search options
        _setupSearchOption();

        //
        _showActionList();

    }
    
    async function search(){
        let classThis = JIRA.WorkflowActionHandler.Variables.Application;

        let data = {
            "isDraft" : classThis._workflowData.isDraft
            , "workflowName" : classThis._workflowData.name
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
            console.log('Variables.searchResult : ', Variables.searchResult);
        }

        let searchOptionContainer = AJS.$('#container-workflow-action-handler-searchbar ul');
        searchOptionContainer.find('.search-option').empty();
        // reverse ( prepend )
        //
        {
            let items = {}
            let transitionMap = Variables.searchResult.maps.transitionMap;
            for(let key in transitionMap){
                let transition = transitionMap[key];
                items[transition['name']] = transition['name'];
            }
            let searchOptionClass = getInstSearchOptionClass('transition-name', 'TransitionName', true, items, {}, true);
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.regEvent();
        }
        //
        {
            let items = {}
            for(let a of Variables.searchResult.actions){
              items[a.transitionId] = a.transitionId;
            }
            let searchOptionClass = getInstSearchOptionClass('transition-id', 'TransitionId', true, items, {}, true);
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.regEvent();
        }
        //
        {
            let optionId = 'action-class-type';
            let items = {'Default' : 'Default', 'JiraBase' : 'JiraBase', 'Custom' : 'Custom',};
            let itemsChecked = {'JiraBase': true,'Custom': true,};
            let searchOptionClass = getInstSearchOptionClass(optionId, 'ActionClassType', false, items, itemsChecked);
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.regEvent();
        }
        //
        {
            let optionId = 'action-type';
            let items = {'Validator' : 'Validator','Condition' : 'Condition','PostFunction' : 'PostFunction', };
            let itemsChecked = {'Condition': true,};
            let searchOptionClass = getInstSearchOptionClass(optionId, 'ActionType', false, items, itemsChecked);
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.regEvent();
        }
        //
        {
            let items = {}
            let itemsChecked = {}
            for(let a of Variables.searchResult.actions){
              items[a.name] = a.name;
            }
            let searchOptionClass = getInstSearchOptionClass('action-name', 'ActionName', true, items, itemsChecked, true);
            let html = searchOptionClass.getHtml();
            searchOptionContainer.prepend(html);
            searchOptionClass.regEvent();
        }
  }

  //
  _setupSearchOption();

  //--------------- end search options ----------- ---
  AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog");
  return JIRA.WorkflowActionHandler.SearchViewDialog;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchViewDialog", null, require("jira-workflow-action-handler/search-view-dialog"));
