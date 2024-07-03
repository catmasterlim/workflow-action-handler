package dev.model;


import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.issue.status.Status;

import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.RestrictionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.List;
import java.util.Map;


@XmlRootElement(name = "WorkflowStepEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowStepEntity {

    @XmlElement(name = "id")
    public int id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "linkStatusId")
    public String linkStatusId;

    public WorkflowStepEntity(StepDescriptor descriptor, JiraWorkflow workflow){

        this.id= descriptor.getId();
        this.name = descriptor.getName();
        Status status = workflow.getLinkedStatus(descriptor);
        this.linkStatusId = workflow.getLinkedStatus(descriptor).getId();
    }


}
