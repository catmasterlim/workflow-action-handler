
define('jira-workflow-action-handler/search-options', [
    "jquery",
    "jira-workflow-action-handler/templates",
    "jira-workflow-action-handler/variables",
    "jira-workflow-action-handler/utils"
], function (
    jQuery,
    Templates,
    Variables,
    Utils
) {

    class SearchOptionClass {
        constructor( optionId, optionLabel, useFindInput, items, selectedItems ){
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

        regEvent(callbackChanged){
            this.callbackChanged = callbackChanged;

            let elDropdown = jQuery(`#${this.optionId}-dropdown`);
            elDropdown.off('change');
            elDropdown.on('change', e => {
                e.preventDefault();
                let isChecked = e.target.hasAttribute('checked');
                let val = e.target.getAttribute('value');
                let text = e.target.textContent;

//                // console.log(('isChecked :',  isChecked);
//                // console.log(('val :',val);
//                // console.log(('text :',text);
//                // console.log(('e.target :',e.target);

                if (isChecked) {
                    this.selectedItems[val] = text;
                } else {
                    delete this.selectedItems[val];
                }
                // console.log(('optionId', this.optionId, 'selectedItems  : ', this.selectedItems);

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
                    // console.log(( 'find value ', val);
                    let elItems = jQuery('#'+this.optionId+'-dropdown .items aui-item-checkbox');
                    for(let item of elItems){
                        // console.log(('item text : ', item.textContent);
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
                selectedOptionText = Utils.truncateString( Utils.arrayFromValues(this.selectedItems).join(','), 15);
            }
            // console.log(('selectedOptionText : ', selectedOptionText);
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
//            this.searchViewClass._changeShowActionBySearchOption();
            if(this.callbackChanged){
                this.callbackChanged();
            }

        }

        _eventSearchOption_find(e){
              let val = e.target.value
              // console.log(( 'find value ', val);

              let items = AJS.$('#action-name-dropdown  aui-item-checkbox')

              for(let item of items){
                // console.log(('item text : ', item.textContent);
                if(val=="" || item.textContent.includes(val) ){
                  item.style.visibility = "visible";
                }else {
                  item.style.visibility = "hidden";
                }
              }

            }
    }


  //--------------- end search options ----------- ---

  // console.log(('----> jira-workflow-action-handler/search-options');
  AJS.namespace("JIRA.WorkflowActionHandler.SearchOptions");

  return SearchOptionClass;
});

AJS.namespace("JIRA.WorkflowActionHandler.SearchOptions", null, require("jira-workflow-action-handler/search-options"));

