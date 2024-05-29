package dev.jira.webwork; 
 
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
 
 
//  import com.atlassian.jira.plugins.workflowdesigner.data.WorkflowDesignerAnalyticsDataProvider;
 
 @WebSudoRequired
 public class WorkflowActionHandlerAction
   extends JiraWebActionSupport
 {
   private final JiraAuthenticationContext authContext;
   private final WorkflowManager workflowManager;
   private final WebResourceAssembler webResourceAssembler;
   private final WorkflowHeaderWebComponent workflowHeaderWebComponent;
//    private final WorkflowDesignerAnalyticsDataProvider workflowDesignerAnalyticsDataProvider;
   private String wfName;
   private String workflowMode;
   private JiraWorkflow workflow;
   private Long project;
   
   @Autowired
   public WorkflowActionHandlerAction(JiraAuthenticationContext jiraAuthenticationContext, PageBuilderService jiraPageBuilderService, WorkflowManager workflowManager) {
     this.authContext = jiraAuthenticationContext;
     this.webResourceAssembler = jiraPageBuilderService.assembler();
     this.workflowManager = workflowManager;
    //  this.workflowDesignerAnalyticsDataProvider = workflowDesignerAnalyticsDataProvider;
     this.workflowHeaderWebComponent = (WorkflowHeaderWebComponent)JiraComponentFactory.getInstance().createObject(WorkflowHeaderWebComponent.class);
   }
 
   
   @SupportedMethods({RequestMethod.GET})
   protected String doExecute() throws Exception {
     return super.doExecute();
   }
 
 
   
   public String execute() {
     ApplicationUser user = this.authContext.getLoggedInUser();
     if (user == null)
     {
       return "notloggedin";
     }
     
     if (!getGlobalPermissionManager().hasPermission(GlobalPermissionKey.ADMINISTER, user))
     {
       return "nopermission";
     }
     
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
    //  this.webResourceAssembler.data().requireData("com.atlassian.jira.plugins.jira-workflow-designer:analytics-data", this.workflowDesignerAnalyticsDataProvider.get());
     
     return "success";
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
   }
 
   
   public String getLinksHtml() {
     return this.workflowHeaderWebComponent.getLinksHtml(getWorkflow(), getProject(), "diagram2", true);
   }
 }

