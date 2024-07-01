{let $hasCheck : $selectedItems?[$key] /}
{if $hasCheck }
<aui-item-checkbox interactive checked value="{$key}" > {$items?[$key]} </aui-item-checkbox>
{else}
<aui-item-checkbox interactive value="{$key}" > {$items?[$key]} </aui-item-checkbox>
{/if}           


/**
* Action Args
* @param action action of workflow
  */
  {template .actionArgs}
  {let $args : $action['args']/}
  {foreach $key in keys($args)}
  {$key} :{$args[$key]}
  {/foreach}
  {/template}


/**
* Action List
* @param isDraft isDraft
* @param workflowName name of workflow
* @param actions name of workflow
* @param transitionMap map of transitions
  */
  {template .actionList}
<div class="results-panel navigator-item">
<div class="navigator-content" >
<table class="aui aui-table-sortable">
    <h2 class="hidden" > {$workflowName} {$isDraft} </h2>
    <thead>
        <tr>
            <th id="alist-action-name" class="aui-table-column-issue-key" >Action Name</th>
            <th id="alist-type">Type</th>
            <th id="alist-order">Order</th>
            <th id="alist-class-name">Class Name</th>
            <th id="alist-class-full">Class Full name</th>
            <th id="alist-args">Args</th>
            <th id="alist-transition-id">TransitionId</th>
            <th id="alist-transition-name">TransitionName</th>
        </tr>
    </thead>
    <tbody>
        {foreach $a in $actions}
            {if not $a.isFiltered}
            <tr>
                <td headers="alist-action-name" value="{$a['name']}" >{$a['name']}</td>
                <th id="alist-type" value="{$a['type']}" >{$a['type']}</th>
                <th id="alist-order" value={$a['order']} >{$a['order']}</th>
                <th id="alist-class-name" value="{$a['classSimpleName']}" >{$a['classSimpleName']}</th>
                <th id="alist-class-full" value={$a['className']} >{$a['className']}</th>
                <th id="alist-args">
                </th>
                <th id="alist-transition-id" value={$a['transitionId']} >{$a['transitionId']}</th>
                {let $transition : $transitionMap[$a['transitionId']] /}
                <th id="alist-transition-name" value={$transition['name']} >{$transition['name']}</th>
            </tr>
            {/if}
        </tr>
        {ifempty}
	        is Empty
        {/foreach} 
    </tbody>
</table>
<div class="aui-item">
    <span class="results-count-text">
        Showing results : {length($actions)} actions of {length(keys($transitionMap))} transitons
    </span>
</div>
</div>
</div>
{/template}

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
