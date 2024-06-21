



/**
 * Action List
 * @param workflowMode isDraft
 * @param workflowName name of workflow
 * @param actions action of workflow
 */
{template .actionList}
<table class="aui aui-table-sortable">
    <thead>
        <tr>
            <th id="action-number" class="aui" >#</th>
            <th id="action-action-name">Action Name</th>
            <th id="action-type">Type</th>
            <th id="action-order">Order</th>
            <th id="action-class-name">Class Name</th>
            <th id="action-transition">Transition</th>
            <th class="">Setting</th>
        </tr>
    </thead>
    <tbody>
        
        {for $action in $actions}
        <tr>
            <td id="action-number" class="aui" >{$action.id}</td>
            <td id="action-action-name">{$action.name}</td>
            <td id="action-type">{$action.type}</th>
            <td id="action-order">{$action.order}</th>
            <td id="action-class-name">{$action.className}</th>
            <td id="action-transition">{$action.transitonId}</th>
        </tr>
        {/for}
        
    </tbody>
</table>
{/template}