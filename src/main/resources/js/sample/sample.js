AJS.toInit(function () {
    console.log('-------- init ------ 2');
    // var $pageMetadata = AJS.$('.workflow-container');

    // if ($pageMetadata.length > 0) {
    //     var selectedAjsParams = {
    //     }   
    //     var template = JIRA.WorkflowActionHandler.Templates.sample.my(selectedAjsParams);       
    //     $pageMetadata.append(template);
    // }

    JIRA.WorkflowActionHandler.Application();
});

console.log('-------- test ------');
