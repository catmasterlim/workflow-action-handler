package dev.jira.jira.tabpanels;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.atlassian.jira.plugin.issuetabpanel.AbstractIssueTabPanel;
import com.atlassian.jira.plugin.issuetabpanel.IssueTabPanel;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.tabpanels.GenericMessageAction;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.atlassian.velocity.VelocityManager;
import com.atlassian.jira.util.VelocityParamFactory;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.issue.RendererManager;
import com.atlassian.jira.issue.fields.renderer.JiraRendererPlugin;

public class MyIssueTabPanel extends AbstractIssueTabPanel
{
    private static final Logger log = LoggerFactory.getLogger(MyIssueTabPanel.class);

    @JiraImport
    private final VelocityManager velocityManager;

    @JiraImport
    private final ApplicationProperties applicationProperties;

    @JiraImport
    private final VelocityParamFactory velocityParamFactory;

    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    @JiraImport
    private final RendererManager rendererManager;
    

    public MyIssueTabPanel(VelocityManager velocityManager,  VelocityParamFactory velocityParamFactory
    , ApplicationProperties applicationProperties
    , JiraAuthenticationContext jiraAuthenticationContext
    , RendererManager rendererManager
    ){
        this.velocityManager = velocityManager;
        this.velocityParamFactory = velocityParamFactory;
        this.applicationProperties = applicationProperties;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.rendererManager = rendererManager;
    }

    public List getActions(Issue issue, ApplicationUser remoteUser) {

        String webworkEncoding = this.applicationProperties.getString("webwork.i18n.encoding");
        String baseUrl = this.applicationProperties.getString("jira.baseurl");

        Map<String, Object> context = this.velocityParamFactory.getDefaultVelocityParams();
        context.put("i18n", this.jiraAuthenticationContext.getI18nHelper());

        JiraRendererPlugin renderer = rendererManager.getRendererForType("atlassian-wiki-renderer");
        String user = renderer.render("[~admin]",  null);
        context.put("user", user);

        String renderedText = this.velocityManager.getEncodedBody("templates/tabpanels/", "my-issue-tab-panel.vm", webworkEncoding, context);
        return Collections.singletonList(new GenericMessageAction(renderedText));
    }

    public boolean showPanel(Issue issue, ApplicationUser remoteUser)
    {
        return true;
    }
}
