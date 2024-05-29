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
 import com.atlassian.jira.component.ComponentAccessor;
 import com.atlassian.webresource.api.assembler.PageBuilderService;
 import com.atlassian.webresource.api.assembler.WebResourceAssembler;
 import org.apache.commons.lang.StringUtils;
 import org.springframework.beans.factory.annotation.Autowired;
//  import org.springframework.beans.factory.annotation.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
 
//  import dev.jira.data.WorkflowActionHandlerAnalyticsDataProvider;
 
 @WebSudoRequired
 public class TestAction
   extends JiraWebActionSupport
 {
   private final JiraAuthenticationContext authContext;

   @JiraImport
   private final WorkflowManager workflowManager;
  //  private final WebResourceAssembler webResourceAssembler;

   private final WorkflowHeaderWebComponent workflowHeaderWebComponent;
  //  private final WorkflowActionHandlerAnalyticsDataProvider analyticsDataProvider;
  //  private String wfName;
  //  private String workflowMode;
  //  private JiraWorkflow workflow;
  //  private Long project;

   @Autowired
   public TestAction(JiraAuthenticationContext jiraAuthenticationContext
    // , PageBuilderService jiraPageBuilderService
    , WorkflowManager workflowManager
    // , WorkflowActionHandlerAnalyticsDataProvider analyticsDataProvider
    ) {
     this.authContext = jiraAuthenticationContext;
    //  this.webResourceAssembler = jiraPageBuilderService.assembler();
     this.workflowManager = workflowManager;
     this.workflowHeaderWebComponent = (WorkflowHeaderWebComponent)JiraComponentFactory.getInstance().createObject(WorkflowHeaderWebComponent.class);
    //  this.analyticsDataProvider = analyticsDataProvider;
   }
 
   
   @SupportedMethods({RequestMethod.GET})
   protected String doExecute() throws Exception {
     return super.doExecute();
   }
 
 }

