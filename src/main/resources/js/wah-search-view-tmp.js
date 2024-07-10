
define('jira-workflow-action-handler/search-view-tmp', [
    "jquery",
    "jira-workflow-action-handler/templates",
    "jira-workflow-action-handler/variables"
], function (
    jQuery,
    Templates,
    Variables
) {

    class SearchOptionClass {
        constructor( searchViewClass,  optionId, optionLabel, useFindInput, items, selectedItems ){
            this.searchViewClass = searchViewClass;
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
                selectedOptionText = this.searchViewClass.truncateString( this.searchViewClass._arrayFromValues(this.selectedItems).join(','), 15);
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
            this.searchViewClass._changeShowActionBySearchOption();

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


    class SearchViewClass {
        constructor(){
            this.doc = jQuery(document);
        }

        init(){
            console.log('---> SearchViewClass : init');
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

            let filterNames = ['action-name', 'action-type', 'action-class-type', 'action-class', 'action-class-simple', 'transition-id', 'transition-name'];
            for(let filterName of filterNames){
                let val = jQuery(trAction).find('#'+filterName).attr('value');
                if( optionMap.get(filterName)?.isFiltered(val)) {
                    console.log( '_isFilteredAction true => ',  ' filterName : ', filterName, ',val : ', val);
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
                searchOptionClass = new SearchOptionClass(this, optionId, optionLabel, useFindInput, items, selectedItems);
                this._getInstSearchOptionSelected().set(optionId, searchOptionClass);
            }

            if(forceRefreshItems){
                searchOptionClass.items = items;
                searchOptionClass.removeSelectedNotIn();
            }

            return searchOptionClass;
        }

        _arrayFromKeys(items, defaultVal){
            if( items === undefined || !items ){
                return defaultVal;
            }

            return Array.from(Object.keys(items));
        }
        _arrayFromValues(items, defaultVal){
                if( items === undefined || !items ){
                    return defaultVal;
                }

                return Array.from(Object.keys(items));
            }

        //
        truncateString(str, maxLength) {
            if (str.length > maxLength) {
                return [str.slice(0, maxLength - 3), '...'].join('');
            }
            return str;
        }

        _showActionList(){

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
            this._changeShowActionBySearchOption();
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
                    isEditable: !!jQuery("#edit-workflow-trigger").length,
                    isInactive: !!jQuery(".status-inactive").length,
                    name: jQuery(".workflow-name").text(),
                    project: jQuery("#workflow-designer-project").val()
                };
        }



        async search(){

            let workflowData = this._getWorkflowData();
            console.log('--> workflowData : ', workflowData);
            let data = {
                "isDraft" : workflowData.isDraft
                , "workflowName" : workflowData.name
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
                console.log('--> search then ok ');
                 Variables.searchResult = result;
                this.searchResultProcess();
                resolve(result);
            })
            ;

        }


      // options selected (checkItems)
      _setupSearchOption(){

            let searchButton = AJS.$('#workflow-action-handler-search-button');
            if(searchButton.length != 1){
                console.error('---> _setupSearchOption : searchButton - not exists #workflow-action-handler-search-button');
            }
            searchButton.off('click');
            searchButton.on('click', e => {
                e.preventDefault();
                console.log('---> search actions');
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

            console.log('Variables.searchResult : ', Variables.searchResult);

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
                let searchOptionClass = this.getInstSearchOptionClass('transition-name', 'TransitionName', true, items, {}, true);
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
                let searchOptionClass = this.getInstSearchOptionClass('transition-id', 'TransitionId', true, items, {}, true);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent();
            }
            //
            {
                let optionId = 'action-class-type';
                let items = {'System' : 'System', 'Bundled' : 'Bundled', 'Custom' : 'Custom',};
                let itemsChecked = {'Bundled': true,'Custom': true,};
                let searchOptionClass = this.getInstSearchOptionClass(optionId, 'ActionClassType', false, items, itemsChecked);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent();
            }
            //
            {
                let optionId = 'action-type';
                let items = {'Validator' : 'Validator','Condition' : 'Condition','PostFunction' : 'PostFunction', };
                let itemsChecked = {'Condition': true,};
                let searchOptionClass = this.getInstSearchOptionClass(optionId, 'ActionType', false, items, itemsChecked);
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
                let searchOptionClass = this.getInstSearchOptionClass('action-name', 'ActionName', true, items, itemsChecked, true);
                let html = searchOptionClass.getHtml();
                searchOptionContainer.prepend(html);
                searchOptionClass.regEvent();
            }
      }
    }



  //--------------- end search options ----------- ---

  console.log('----> jira-workflow-action-handler/search-view-tmp');
  AJS.namespace("JIRA.WorkflowActionHandler.SearchViewTmp");

  let app = new SearchViewClass();
  return app;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchViewTmp", null, require("jira-workflow-action-handler/search-view-tmp"));

