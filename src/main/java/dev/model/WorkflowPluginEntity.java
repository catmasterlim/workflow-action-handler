package dev.model;


import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.plugin.Plugin;
import com.opensymphony.workflow.loader.StepDescriptor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "WorkflowPluginEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowPluginEntity {

    @XmlElement(name = "key")
    public String key;

    @XmlElement(name = "name")
    public String name;


    @XmlElement(name = "isBundledPlugin")
    public boolean isBundledPlugin;

    private WorkflowPluginEntity(){
        this.key = "";
        this.name = "Unknown";
        this.isBundledPlugin = false;
    }


    public WorkflowPluginEntity(Plugin plugin){

        this.key = plugin.getKey();
        this.name = plugin.getName();
        this.isBundledPlugin = plugin.isBundledPlugin();
    }

    public static  WorkflowPluginEntity CreateUnknownPluginEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        entity.key = "";
        entity.name = "Unknown";
        entity.isBundledPlugin = false;
        return  entity;
    }
    public static  WorkflowPluginEntity CreateJiraPluginEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        entity.key = "com.atlassian.jira";
        entity.name = "BundledPlugin";
        entity.isBundledPlugin = true;
        return  entity;
    }

}
