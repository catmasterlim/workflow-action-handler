package dev.jira.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.workflow.edit.Workflows;
import com.atlassian.plugin.spring.scanner.annotation.component.JiraComponent;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.google.gson.JsonParser;
import com.atlassian.jira.workflow.edit.Workflow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * A resource of message.
 */
@Path("/workflow")
public class WorkflowActionHandlerResource {

    @JiraImport
    private final Workflows workflows;

    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public WorkflowActionHandlerResource(
            Workflows workflows, JiraAuthenticationContext jiraAuthenticationContext) {
        this.workflows = workflows;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getWorkflow(
            @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft,
            @QueryParam("workflowName") String workflowName) {

        // TODO : Test data
        // try {
            // InputStream stream = getClass().getClassLoader().getResourceAsStream("/data/sample/sample.json");                        
            // return Response.ok(stream).build();

        // } catch (Exception e) {
        //     return Response.ok(e.toString()).build();
        // }

        Workflow w = this.workflows.getWorkflow(isDraft, workflowName);

        return Response.ok(new WorkflowActionHandlerResourceModel(workflowName,
        isDraft, w)).build();
    }

}