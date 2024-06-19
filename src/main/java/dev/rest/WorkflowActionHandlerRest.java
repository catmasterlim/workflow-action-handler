package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.jira.workflow.WorkflowManager;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * actions list
 */
@Path("/")
public class WorkflowActionHandlerRest {


    @JiraImport
    private final WorkflowManager workflowManager;

    @JiraImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    public WorkflowActionHandlerRest( WorkflowManager workflowManager, JiraAuthenticationContext jiraAuthenticationContext) {
        this.workflowManager = workflowManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }


    @GET
    @Path("/test")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getTest(
            @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft,
            @QueryParam("workflowName") String workflowName) {


        return Response.ok(
                "isDraft : " + isDraft
        ).build();
    }

    @GET
    @Path("/actions")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getActions(
            @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft,
            @QueryParam("workflowName") String workflowName) {

        // TODO : Test data
        // try {
        // InputStream stream = getClass().getClassLoader().getResourceAsStream("/data/sample/sample.json");
        // return Response.ok(stream).build();

        // } catch (Exception e) {
        //     return Response.ok(e.toString()).build();
        // }

        JiraWorkflow w = this.workflowManager.getWorkflow(workflowName);
        if(isDraft){
            w = this.workflowManager.getDraftWorkflow(workflowName);
        }

        return Response.ok(
                new WorkflowActionHandlerRestModel(
                        workflowName,
                    isDraft, w
                )
        ).build();
    }
}