package dev.data;

 import com.atlassian.jira.permission.GlobalPermissionKey;
 import com.atlassian.jira.permission.ProjectPermissionHelper;
 import com.atlassian.jira.security.GlobalPermissionManager;
 import com.atlassian.jira.security.JiraAuthenticationContext;
 import com.atlassian.jira.util.json.JSONException;
 import com.atlassian.jira.util.json.JSONObject;
 import com.atlassian.jira.web.ExecutingHttpRequest;
 import com.atlassian.jira.workflow.ProjectWorkflowSchemeHelper;
 import com.atlassian.json.marshal.Jsonable;
 import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
 import com.atlassian.webresource.api.data.WebResourceDataProvider;
 import com.google.common.collect.ImmutableMap;
 import dev.api.WorkflowActionHandlerAnalyticsDataProvider;

 import java.io.IOException;
 import java.io.Writer;
 import java.util.Map;
 import java.util.Optional;


//@Component
public class WorkflowActionHandlerAnalyticsDataProviderImpl
        implements WorkflowActionHandlerAnalyticsDataProvider
{
    @JiraImport
    private final ProjectPermissionHelper projectPermissionHelper;

    @JiraImport
    private final GlobalPermissionManager globalPermissionManager;

    @JiraImport
    private final ProjectWorkflowSchemeHelper projectWorkflowSchemeHelper;

    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;


    public WorkflowActionHandlerAnalyticsDataProviderImpl(ProjectPermissionHelper projectPermissionHelper, GlobalPermissionManager globalPermissionManager, ProjectWorkflowSchemeHelper projectWorkflowSchemeHelper, JiraAuthenticationContext jiraAuthenticationContext) {
        this.projectPermissionHelper = projectPermissionHelper;
        this.globalPermissionManager = globalPermissionManager;
        this.projectWorkflowSchemeHelper = projectWorkflowSchemeHelper;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @Override
    public Jsonable get() {
        return new Jsonable()
        {
            public void write(Writer writer) throws IOException {
                try {
                    WorkflowActionHandlerAnalyticsDataProviderImpl.this.getJsonData().write(writer);
                } catch (JSONException e) {
                    throw new Jsonable.JsonMappingException(e);
                }
            }
        };
    }

    JSONObject getJsonData() {
        return new JSONObject((Map)ImmutableMap.of("isolated",
                getWorkflowName().<Boolean>map(workflowName -> Boolean.valueOf(this.projectWorkflowSchemeHelper.isWorkflowIsolated(workflowName))).orElse(Boolean.valueOf(false)), "hasExtPermission",
                getWorkflowName().<Boolean>map(workflowName -> Boolean.valueOf(this.projectPermissionHelper.hasExtPermission(this.projectWorkflowSchemeHelper.getAllProjectsForWorkflow(workflowName)))).orElse(Boolean.valueOf(false)), "isAdmin",
                Boolean.valueOf(this.globalPermissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, this.jiraAuthenticationContext.getLoggedInUser()))));
    }


    private Optional<String> getWorkflowName() {
        return Optional.ofNullable(ExecutingHttpRequest.get().getParameter("wfName"));
    }

//    @Override
//    public Jsonable get() {
//        return new JsonableString("Hello world!");
//    }
}
