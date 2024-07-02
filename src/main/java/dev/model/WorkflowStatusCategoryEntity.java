package dev.model;


import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.issue.status.category.StatusCategory;
import com.atlassian.jira.workflow.JiraWorkflow;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement(name = "WorkflowStatusCategoryEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowStatusCategoryEntity {

    @XmlElement(name = "id")
    public Long id;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "aliases")
    public List<String> aliases;

    @XmlElement(name = "colorName")
    public String colorName;

    @XmlElement(name = "translatedName")
    public String translatedName;

    @XmlElement(name = "sequence")
    public Long sequence;


    public WorkflowStatusCategoryEntity(StatusCategory statusCategory, JiraWorkflow workflow){

        this.id = statusCategory.getId();
        this.name = statusCategory.getName();

        this.aliases = statusCategory.getAliases();
        this.colorName = statusCategory.getColorName();

        this.translatedName = statusCategory.getTranslatedName();

        this.sequence = statusCategory.getSequence();
    }


}
