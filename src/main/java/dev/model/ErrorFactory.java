package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.ErrorManager;
import java.util.stream.Collectors;

public class ErrorFactory {
    public static ErrorModel CreateUnknownError(){
        ErrorModel model = new ErrorModel();
        return model;
    }

    public static ErrorModel CreateNoLoginError(){
        ErrorModel model = new ErrorModel(400, "Login", "로그인 상태가 아님", "");
        return model;
    }


    public static ErrorModel CreatePermissionError(){
        ErrorModel model = new ErrorModel(400, "PermissionError", "권한이 없음", "");
        return model;
    }

    public static ErrorModel CreateNoDraftError(){
        ErrorModel model = new ErrorModel(400, "NoDraft", "workflow draft가 없음", "");
        return model;
    }
    public static ErrorModel CreateNoWorkflowError(String workflowName){
        ErrorModel model = new ErrorModel(400, "NoWorkflow"
        , String.format("해당 워크플로우({})가 존재하지 않습니다.", workflowName)
        , "");
        return model;
    }
}
