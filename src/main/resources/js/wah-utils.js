define('jira-workflow-action-handler/utils', [
    "jquery"
], function (
    jQuery
) {

    class Utils {
        arrayFromKeys(items, defaultVal){
            if( items === undefined || !items ){
                return defaultVal;
            }

            return Array.from(Object.keys(items));
        }
        arrayFromValues(items, defaultVal){
            if( items === undefined || !items ){
                return defaultVal;
            }

            return Array.from(Object.values(items));
        }

        //
        truncateString(str, maxLength) {
            if (str.length > maxLength) {
                return [str.slice(0, maxLength - 3), '...'].join('');
            }
            return str;
        }

        getDraftToken(){
            if( jQuery('#publish-draft').length  == 0) {
                return '';
            }

            try{
                let token = new URLSearchParams(jQuery('#discard-draft').attr('href')).get('atl_token');
                if(token == undefined){
                    return ''
                }

                return token;

            } catch (err){

            }

            return '';
        }

        searchAction(){
            AJS.$('#workflow-action-handler-search-button').trigger('click');
        }

    }


    console.log('----> jira-workflow-action-handler/utils');
    // make sure that the template namespace is in place
    AJS.namespace("JIRA.WorkflowActionHandler.Utils");

    let utils = new Utils();
    return utils;
});

AJS.namespace("JIRA.WorkflowActionHandler.Utils", null, require('jira-workflow-action-handler/utils'));
