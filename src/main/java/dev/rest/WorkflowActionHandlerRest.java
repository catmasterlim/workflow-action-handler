package dev.rest;

import com.atlassian.jira.config.ResolutionManager;
import com.atlassian.jira.config.StatusCategoryManager;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.permission.ProjectPermission;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.json.JSONObject;
import com.atlassian.jira.util.json.JsonUtil;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.jira.config.ConstantsManager;
//import com.atlassian.jira.component.ComponentAccessor;


import com.atlassian.sal.api.user.UserKey;
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

    @JiraImport
    private final  com.atlassian.sal.api.user.UserManager userManager;

    @JiraImport
    private final ConstantsManager constantsManager;

    @JiraImport
    private final StatusCategoryManager statusCategoryManager;

    @JiraImport
    private final  com.atlassian.plugin.PluginAccessor pluginAccessor;

    @JiraImport
    private final ResolutionManager resolutionManager;

    @JiraImport
    private final com.atlassian.jira.security.PermissionManager permissionManager;


    public WorkflowActionHandlerRest(WorkflowManager workflowManager, JiraAuthenticationContext jiraAuthenticationContext, com.atlassian.sal.api.user.UserManager userManager, ConstantsManager constantsManager, StatusCategoryManager statusCategoryManager, com.atlassian.plugin.PluginAccessor pluginAccessor, ResolutionManager resolutionManager, com.atlassian.jira.security.PermissionManager permissionManager) {
        this.workflowManager = workflowManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
        this.userManager = userManager;
        this.constantsManager = constantsManager;
        this.statusCategoryManager = statusCategoryManager;
        this.pluginAccessor = pluginAccessor;
        this.resolutionManager = resolutionManager;
        this.permissionManager = permissionManager;

        Const.setPluginAccessor(this.pluginAccessor);
        Const.setResolutionManager(this.resolutionManager);
    }



    /**
     * getActions
     * @param includedFiltered 필터된 action 결과에 포함여부, default : true
     * @param workflowMode  live , draft
     * @param workflowName 워크프로우 이름
     * @param actionName 액션 이름 리스트 ( stirng [] )
     * @param actionType 액션 타입 리스트 ( stirng [] )
     * @return Response
     */
    @GET
    @Path("/actions")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getActions(
            @DefaultValue("false") @QueryParam("includedSystem") Boolean includedSystem
            , @DefaultValue("true") @QueryParam("includedFiltered") Boolean includedFiltered
            , @DefaultValue("live") @QueryParam("workflowMode") String workflowMode
            , @QueryParam("workflowName") String workflowName
            , @QueryParam("actionName") List<String> actionName
            , @QueryParam("actionType") List<String> actionType
            , @QueryParam("transitionId") List<Integer> transitionId
            , @QueryParam("transitionName") List<String> transitionName
           ) {

        log.info("params - includedSystem : {}", includedSystem);
        log.info("params - includedFiltered : {}", includedFiltered);
        log.info("params - workflowMode : {}", workflowMode);
        log.info("params - workflowName : {}", workflowName);
        log.info("params - actionName : {}", actionName);
        log.info("params - actionType : {}", actionType);
        log.info("params - transitionId : {}", transitionId);
        log.info("params - transitionName : {}", transitionName);


        ApplicationUser user = this.jiraAuthenticationContext.getLoggedInUser();
        if (user == null)
        {
            return Response.serverError()
                    .status(Status.UNAUTHORIZED)
                    .entity( ErrorFactory.CreatePermissionError() )
                    .build();
        }

        UserKey userKey = new UserKey(user.getKey());
        if( this.userManager.isAdmin(userKey) == false){
            return Response.serverError()
                    .status(Status.UNAUTHORIZED)
                    .entity( ErrorFactory.CreatePermissionError() )
                    .build();
        }

        // filter 
        WorkflowActionFilterModel filterModel = new WorkflowActionFilterModel();
        filterModel.addFilterActionNameAll(actionName);
        filterModel.addFilterActionTypeAll(actionType);
        filterModel.addFilterTransitionIdAll(transitionId);
        filterModel.addFilterTransitionNameAll(transitionName);

        JiraWorkflow workflow = this.workflowManager.getWorkflow(workflowName);
        if(workflow == null){
            return Response.serverError()
                    .status(Status.BAD_REQUEST)
                    .entity( ErrorFactory.CreateNoWorkflowError(workflowName) )
                    .build();
        }

        if(!workflowMode.equals("live") && !workflow.hasDraftWorkflow()){
            return Response.serverError()
                    .status(Status.BAD_REQUEST)
                    .entity( ErrorFactory.CreateNoDraftError() )
                    .build();
        }
        
        if(!workflowMode.equals("live")){
            workflow = this.workflowManager.getDraftWorkflow(workflowName);
        }

        return Response.ok(new WorkflowActionModel(constantsManager, statusCategoryManager, includedSystem, includedFiltered, workflow, workflowMode, filterModel)).build();
    }
}