package dev.jira.webwork;

import com.atlassian.config.bootstrap.AtlassianBootstrapManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.BaseUrl;
import com.atlassian.jira.web.ExecutingHttpRequest;
import com.atlassian.jira.web.component.WorkflowHeaderWebComponent;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.websudo.WebSudoRequired;
import com.atlassian.webresource.api.assembler.WebResourceAssembler;
//import dev.data.WorkflowActionHandlerAnalyticsDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.JiraComponentFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.web.component.WorkflowHeaderWebComponent;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.sal.api.websudo.WebSudoRequired;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.atlassian.webresource.api.assembler.WebResourceAssembler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@SupportedMethods({RequestMethod.GET})
@WebSudoRequired
public class WorkflowActionSearchAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(WorkflowActionSearchAction.class);

    private String wfName;
    private String workflowMode;
    private JiraWorkflow workflow;
    private Long project;

    private final ApplicationProperties applicationProperties;
    private final JiraAuthenticationContext jiraAuthenticationContext;
    private final WorkflowManager workflowManager;
//    private final WebResourceAssembler webResourceAssembler;
//    private final WorkflowActionHandlerAnalyticsDataProvider workflowActionHandlerAnalyticsDataProvider;

    @Autowired
    public WorkflowActionSearchAction(  @JiraImport ApplicationProperties applicationProperties
            , JiraAuthenticationContext jiraAuthenticationContext
            , WorkflowManager workflowManager){
        this.applicationProperties = applicationProperties;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.workflowManager = workflowManager;
    }

    @SupportedMethods({RequestMethod.GET})
    @Override
    public String execute() throws Exception {

        ApplicationUser user = this.jiraAuthenticationContext.getLoggedInUser();
        if (user == null)
        {
            return "notloggedin";
        }

        if (!this.getGlobalPermissionManager().hasPermission(GlobalPermissionKey.ADMINISTER, user))
        {
            return "nopermission";
        }

        this.wfName = ExecutingHttpRequest.get().getParameter("workflowName");
        this.workflowMode = ExecutingHttpRequest.get().getParameter("workflowMode");

        if (StringUtils.isEmpty(this.wfName) || StringUtils.isEmpty(this.workflowMode) || (!this.workflowMode.equals("live") && !this.workflowMode.equals("draft")))
        {
            return "invalidworkflowname";
        }

        if ((this.workflowMode.equals("live") && this.workflowManager.getWorkflow(this.wfName) == null) || (this.workflowMode
                .equals("draft") && this.workflowManager.getDraftWorkflow(this.wfName) == null))
        {
            return "invalidworkflowname";
        }
        return INPUT;

//        return SUCCESS;
    }

    public String getWfName() {
        return this.wfName;
    }


    public void setWfName(String wfName) {
        this.wfName = wfName;
    }


    public void setWorkflowMode(String workflowMode) {
        this.workflowMode = workflowMode;
    }


    public JiraWorkflow getWorkflow() {
        if (this.workflow == null)
        {
            if (this.workflowMode.equals("live")) {

                this.workflow = this.workflowManager.getWorkflow(this.wfName);
            }
            else {

                this.workflow = this.workflowManager.getDraftWorkflow(this.wfName);
            }
        }

        return this.workflow;
    }


    public Long getProject() {
        return this.project;
    }


    public void setProject(Long project) {
        this.project = project;
    }

    public String getHeaderHtml() {
//        return this.workflowHeaderWebComponent.getHtml(getWorkflow(), "workflow_designer", getProject());
        return  "";
    }
//
//
//    public String getLinksHtml() {
//        return this.workflowHeaderWebComponent.getLinksHtml(getWorkflow(), getProject(), "diagram", true);
//    }

//    public String getContextPath(){
//        return this.atlassianBootstrapManager.getApplicationHome();
//    }

    public String getBaseurl(){
        return this.applicationProperties.getString("baseurl");
    }
}
