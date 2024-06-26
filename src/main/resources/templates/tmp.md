            <aui-section label="">
                <li class="check-list-group-actions"><a class="clear-all" href="#">Clear selected items</a></li>
            </aui-section>


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

        {call .searchOption}
            {param optionId: 'action-class-type' /}
            {param optionLabel : 'ActionClassType' /}
            {param items: [
                "Default" : "Default""
                , "JiraBase" : "JiraBase"
                , "Custom" : "Custom"
            ] /}
        {/call}
