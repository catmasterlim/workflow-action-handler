package dev.model;


import com.atlassian.plugin.Plugin;

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

    @XmlElement(name = "isSystemPlugin")
    public boolean isSystemPlugin;

    @XmlElement(name = "isBundledPlugin")
    public boolean isBundledPlugin;

    private WorkflowPluginEntity(){
        this.key = "";
        this.name = "Unknown";
        this.isSystemPlugin = false;
        this.isBundledPlugin = false;
    }

    public WorkflowPluginEntity(Plugin plugin){
        this.key = plugin.getKey();
        this.name = plugin.getName();
        this.isSystemPlugin = false;
        this.isBundledPlugin = plugin.isBundledPlugin();
    }

    public static  WorkflowPluginEntity CreateUnknownPluginEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        entity.key = "";
        entity.name = "Unknown";
        entity.isBundledPlugin = false;
        return  entity;
    }
    public static  WorkflowPluginEntity CreateJiraBundlePluginEntity(WorkflowPluginEntity pluginEntity){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        if( pluginEntity == null ) {
            entity.key = "com.atlassian.jira.plugin.bundled";
            entity.name = "Jira Bundled Plugin";
        } else {
            entity.key = pluginEntity.key;
            entity.name = pluginEntity.name;
        }
        entity.name = "Jira Bundled Plugin";
        entity.isSystemPlugin = false;
        entity.isBundledPlugin = true;
        return  entity;
    }
    public static  WorkflowPluginEntity CreateJiraSystemEntity(WorkflowPluginEntity pluginEntity){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        if( pluginEntity == null ) {
            entity.key = "com.atlassian.jira.plugin.bundled";
            entity.name = "Jira Bundled Plugin";
        } else {
            entity.key = pluginEntity.key;
            entity.name = pluginEntity.name;
        }
        entity.name = "Jira Bundled Plugin";
        entity.isSystemPlugin = true;
        entity.isBundledPlugin = true;
        return  entity;
    }

}
