package dev.model;


import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.StepDescriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.atlassian.jira.issue.status.category.StatusCategory;
import com.atlassian.jira.issue.status.SimpleStatus;


@XmlRootElement(name = "WorkflowStatusEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowStatusEntity {

    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "translatedName")
    public String translatedName;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "iconUrl")
    public String iconUrl;

    @XmlElement(name = "statusCategoryId")
    public long statusCategoryId;


    public WorkflowStatusEntity(Status status, JiraWorkflow workflow){

        this.id = status.getId();
        this.name = status.getName();
        this.translatedName = status.getNameTranslation();

        this.description = status.getSimpleStatus().getDescription();
        this.iconUrl = status.getSimpleStatus().getIconUrl();

        this.statusCategoryId = status.getStatusCategory().getId();
    }


}
