package dev.data;

import com.atlassian.jira.help.HelpUrl;
import com.atlassian.jira.help.HelpUrls;
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
import com.google.common.collect.ImmutableMap;
import dev.api.WorkflowActionHandlerAnalyticsDataProvider;
import dev.api.WorkflowActionHandlerHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Optional;


//@Component
public class WorkflowActionHandleHelperImpl
        implements WorkflowActionHandlerHelper
{
//    public static final String ADVANCED_WORKFLOW_CONFIGURATION_HELP_URL = "workflow.action.handler.advanced.workflow.configuration";

    @JiraImport
    private final HelpUrls helpUrls;


    public WorkflowActionHandleHelperImpl(HelpUrls helpUrls) {
        this.helpUrls = helpUrls;
    }

    @Override
    public HelpUrl getLearnMoreHelpUrl() {
        return this.helpUrls.getUrl("workflow.action.handler.advanced.workflow.configuration");
    }

}
