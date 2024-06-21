package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.util.json.JsonUtil;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.jira.workflow.WorkflowManager;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import dev.model.*;


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

        try {
            
            JSONObject obj = new JSONObject();
            obj.put("workflowName", workflowName);
            
            return Response.ok(
                obj.toString()
            ).build();
        }catch(Exception ex){
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).build();
        }
    }

    @GET
    @Path("/transitions")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getTransitions(
            @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft,
            @QueryParam("workflowName") String workflowName) {

        JiraWorkflow workflow = this.workflowManager.getWorkflow(workflowName);
        // TODO : 권한 체크

        // TODO : draft 가 없을 경우 에러 처리 
        if(isDraft && !workflow.hasDraftWorkflow()){
            return Response.ok( ErrorFactory.CreateNoDraftError() ).status(Status.BAD_REQUEST).build();
        }
        
        if(isDraft){
            workflow = this.workflowManager.getDraftWorkflow(workflowName);
        }

        return Response.ok( new WorkflowTransitionModel(workflow, isDraft)).build();
    }

    @GET
    @Path("/actions")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getActions(
            @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft,
            @QueryParam("workflowName") String workflowName) {

        JiraWorkflow workflow = this.workflowManager.getWorkflow(workflowName);
        // TODO : 권한 체크

        // TODO : draft 가 없을 경우 에러 처리 
        if(isDraft && !workflow.hasDraftWorkflow()){
            return Response.ok( ErrorFactory.CreateNoDraftError() ).status(Status.BAD_REQUEST).build();
        }
        
        if(isDraft){
            workflow = this.workflowManager.getDraftWorkflow(workflowName);
        }

        return Response.ok( new WorkflowActionModel(workflow, isDraft)).build();
    }
}