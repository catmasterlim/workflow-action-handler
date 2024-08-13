package dev.model;


import com.atlassian.plugin.Plugin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Optional;


@XmlRootElement(name = "WorkflowPluginEntity")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkflowPluginEntity {

    @XmlElement(name = "key")
    public String key;

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "in_key")
    public String in_key;

    @XmlElement(name = "in_name")
    public String in_name;

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



    public static WorkflowPluginEntity Create(String classFullName, Optional<Plugin> plugin){

        boolean isBundledPlugin = Const.isBundledClassType(classFullName);
        boolean isSystemPlugin = Const.isSystemClassType(classFullName);

        WorkflowPluginEntity pluginEntity = null;
        if(isBundledPlugin){
            if( isSystemPlugin ) {
                pluginEntity = CreateJiraBundlePluginEntity();
            } else {
                pluginEntity = CreateJiraSystemEntity();
            }
        } else {
            pluginEntity = CreateUnknownPluginEntity();
        }

        if(plugin.isPresent()) {
            if (isBundledPlugin == false) {
                pluginEntity.key = plugin.get().getKey();
                pluginEntity.name = plugin.get().getName();
            } else {
                pluginEntity.in_key = plugin.get().getKey();
                pluginEntity.in_name = plugin.get().getName();
            }
        }

        return pluginEntity;
    }


    public static  WorkflowPluginEntity CreateUnknownPluginEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        entity.key = "";
        entity.name = "Unknown";
        entity.isBundledPlugin = false;
        return  entity;
    }
    public static  WorkflowPluginEntity CreateJiraBundlePluginEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();

        entity.key = "com.atlassian.jira.plugin.bundled";
        entity.name = "Jira Bundled Plugin";
        entity.isSystemPlugin = false;
        entity.isBundledPlugin = true;
        return  entity;
    }
    public static  WorkflowPluginEntity CreateJiraSystemEntity(){
        WorkflowPluginEntity entity = new WorkflowPluginEntity();
        entity.key = "com.atlassian.jira.plugin.bundled";
        entity.name = "Jira Bundled Plugin";
        entity.isSystemPlugin = true;
        entity.isBundledPlugin = true;
        return  entity;
    }

}
