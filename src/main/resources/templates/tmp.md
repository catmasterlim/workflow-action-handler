{let $hasCheck : $selectedItems?[$key] /}
{if $hasCheck }
<aui-item-checkbox interactive checked value="{$key}" > {$items?[$key]} </aui-item-checkbox>
{else}
<aui-item-checkbox interactive value="{$key}" > {$items?[$key]} </aui-item-checkbox>
{/if}           


<aui-section label="">
                <li class="check-list-group-actions"><a class="clear-all" href="#">Clear selected items</a></li>
            </aui-section>

/**
* Search Option
* @param optionId id of option
* @param optionLabel label of option(display)
* @param? useFindInput find input - true, false
* @param items item of option map ( key, value )
* @param? selectedItems checked item list (set)
  */
{call .searchOption}
{param optionId: 'action-name' /}
{param optionLabel : 'ActionName' /}
{param items: [] /}
{/call}
{call .searchOption}
{param optionId: 'action-type' /}
{param optionLabel : 'ActionType' /}
{param items: [
"Validator" : "Validator""
, "Condition" : "Condition"
, "PostFunction" : "PostFunction"
] /}
{/call}
* 
* 
* 
*

      {let $actionNames: ['Validator' : 'Validator', 'Condition' : 'Condition', 'PostFunction' : 'PostFunction']/}
      {call .searchOption}
          {param optionId: 'action-name' /}
          {param optionLabel : 'ActionName' /}
          {param useFindInput : true /}
          {param items: $tempMap /}
          {param selectedItems: $tempList /}
      {/call}


        {let $actionTypes: ['Validator' : 'Validator', 'Condition' : 'Condition', 'PostFunction' : 'PostFunction']/}
        {call .searchOption}
            {param optionId: 'action-type' /}
            {param optionLabel : 'ActionType' /}
            {param useFindInput : false /}
            {param items: $actionTypes /}
            {param selectedItems: $tempList /}
        {/call}

        {let $actionClassTypes: ['Default' : 'Default' , 'JiraBase' : 'JiraBase', 'Custom' : 'Custom']/}
        {call .searchOption}
            {param optionId: 'action-class-type' /}
            {param optionLabel : 'ActionClassType' /}
            {param useFindInput : false /}
            {param items: $actionClassTypes /}
            {param selectedItems: $tempList /}
        {/call}

        {call .searchOption}
            {param optionId: 'transition-id' /}
            {param optionLabel : 'Transition' /}
            {param useFindInput : false /}
            {param items: $tempMap /}
            {param selectedItems: $tempList /}
        {/call}

        {call .searchOption}
            {param optionId: 'transition-name' /}
            {param optionLabel : 'TransitionName' /}
            {param useFindInput : true /}
            {param items: $tempMap /}
            {param selectedItems: $tempList /}
        {/call}

        {call .searchOption}
            {param optionId: 'action-class-type' /}
            {param optionLabel : 'ActionClassType' /}
            {param items: [
                "Default" : "Default""
                , "JiraBase" : "JiraBase"
                , "Custom" : "Custom"
            ] /}
        {/call}
