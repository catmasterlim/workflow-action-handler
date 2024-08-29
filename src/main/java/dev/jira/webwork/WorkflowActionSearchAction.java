package dev.jira.webwork;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.ExecutingHttpRequest;
import com.atlassian.jira.web.component.WorkflowHeaderWebComponent;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.sal.api.websudo.WebSudoRequired;
import com.atlassian.webresource.api.assembler.WebResourceAssembler;

import javax.inject.Inject;
import javax.ws.rs.GET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.util.JiraComponentFactory;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.webresource.api.assembler.PageBuilderService;

import org.springframework.util.StringUtils;

public class WorkflowActionSearchAction extends JiraWebActionSupport
{
    private static final Logger log = LoggerFactory.getLogger(WorkflowActionSearchAction.class);

    private String wfName;
    private String workflowMode;
    private JiraWorkflow workflow;
    private Long project;

    @JiraImport
    private final ApplicationProperties applicationProperties;

    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    @JiraImport
    private final WorkflowManager workflowManager;

    private final WebResourceAssembler webResourceAssembler;

    private final WorkflowHeaderWebComponent workflowHeaderWebComponent;

   @Inject
    public WorkflowActionSearchAction(
            ApplicationProperties applicationProperties
            , JiraAuthenticationContext jiraAuthenticationContext
            , WorkflowManager workflowManager
            , @JiraImport PageBuilderService pageBuilderService

    ){
        this.applicationProperties = applicationProperties;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.workflowManager = workflowManager;
        this.webResourceAssembler = pageBuilderService.assembler();

        this.workflowHeaderWebComponent = (WorkflowHeaderWebComponent)JiraComponentFactory.getInstance().createObject(WorkflowHeaderWebComponent.class);;
    }

    @GET
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

        this.webResourceAssembler.resources().requireContext("jira.workflow.view");

        return SUCCESS;
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
        return this.workflowHeaderWebComponent.getHtml(getWorkflow(), "workflow_action_handler", getProject());
//        return  "";
    }
//
//
    public String getLinksHtml() {
        return this.workflowHeaderWebComponent.getLinksHtml(getWorkflow(), getProject(), "diagram", true);
    }

//    public String getContextPath(){
//        return this.atlassianBootstrapManager.getApplicationHome();
//    }

    public String getBaseurl(){
        return this.applicationProperties.getString("baseurl");
    }
}
