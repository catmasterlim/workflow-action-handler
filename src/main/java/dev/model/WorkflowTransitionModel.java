package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement(name = "transitions")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowTransitionModel {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "isDraft")
    public boolean isDraft;

    @XmlElement(name = "isActive")
    public boolean isActive;

    @XmlElement(name = "transitions")
    public final List<WorkflowTransitionEntity> transitions = new ArrayList<>();

    private final Map<Integer, WorkflowTransitionEntity> transitionMap = new HashMap<>();

    public WorkflowTransitionModel() {
    }

    public WorkflowTransitionModel(JiraWorkflow workflow, boolean isDraft) {
        this.name = workflow.getName();
        this.isDraft = isDraft;
        this.description = workflow.getDescription();

        for(ActionDescriptor transition :  workflow.getAllActions()){
            WorkflowTransitionEntity entity = new WorkflowTransitionEntity(transition, workflow);
            this.transitionMap.put(entity.id,  entity);
            this.transitions.add(entity);
        }
//        this.transitions = workflow.getAllActions().stream().map( transition -> new WorkflowTransitionEntity(transition, workflow) ).collect(Collectors.toList());

    }

    public WorkflowTransitionEntity getTransitionEntity(Integer id){
        return transitionMap.get(id);
    }
}