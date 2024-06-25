package dev.rest;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.util.json.JsonUtil;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.jira.workflow.WorkflowManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import dev.model.*;


/**
 * actions list
 */
@Path("/")
public class WorkflowActionHandlerRest {

    private static final Logger log = LoggerFactory.getLogger(WorkflowActionHandlerRest.class);

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

    /**
     * getActions
     * @param includedFiltered 필터된 action 결과에 포함여부, default : true
     * @param isDraft 편집 모드
     * @param workflowName 워크프로우 이름
     * @param paramActionName 액션 이름 리스트 ( stirng [] )
     * @param paramActionType 액션 타입 리스트 ( stirng [] )
     * @return Response
     */
    @GET
    @Path("/actions")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getActions(
            @DefaultValue("true") @QueryParam("includedFiltered") Boolean includedFiltered
            , @DefaultValue("false") @QueryParam("isDraft") Boolean isDraft
            , @QueryParam("workflowName") String workflowName
            , @QueryParam("actionName") List<String> paramActionName
            , @QueryParam("actionType") List<String> paramActionType
            , @QueryParam("actionClassType") List<String> paramActionClassType
           ) {

        log.info("params - includedFiltered : {}", includedFiltered);
        log.info("params - isDraft : {}", isDraft);
        log.info("params - workflowName : {}", workflowName);
        log.info("params - actionName : {}", paramActionName);
        log.info("params - actionType : {}", paramActionType);
        log.info("params - actionClassType : {}", paramActionClassType);

        // filter 
        WorkflowActionFilterModel filterModel = new WorkflowActionFilterModel();
        filterModel.addFilterActionNameAll(paramActionName);
        filterModel.addFilterActionTypeAll(paramActionType);
        filterModel.addFilterActionClassTypeAll(paramActionClassType);

        JiraWorkflow workflow = this.workflowManager.getWorkflow(workflowName);
        if(workflow == null){
            return Response.ok( ErrorFactory.CreateNoWorkflowError(workflowName) ).status(Status.BAD_REQUEST).build();
        }

        // TODO : 권한 체크

        // TODO : draft 가 없을 경우 에러 처리 
        if(isDraft && !workflow.hasDraftWorkflow()){
            return Response.ok( ErrorFactory.CreateNoDraftError() ).status(Status.BAD_REQUEST).build();
        }
        
        if(isDraft){
            workflow = this.workflowManager.getDraftWorkflow(workflowName);
        }

        return Response.ok(new WorkflowActionModel(includedFiltered, workflow, isDraft, filterModel)).build();
    }
}