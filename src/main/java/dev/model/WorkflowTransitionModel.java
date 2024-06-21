package dev.model;

import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    public List<WorkflowTransitionEntity> transitions;

    public WorkflowTransitionModel() {
    }

    public WorkflowTransitionModel(JiraWorkflow workflow, boolean isDraft) {
        this.name = workflow.getName();
        this.isDraft = isDraft;
        this.description = workflow.getDescription();
        Collection<ActionDescriptor> actions = workflow.getAllActions();

        transitions = actions.stream().map( transition -> new WorkflowTransitionEntity(transition, workflow) ).collect(Collectors.toList());
    }
}