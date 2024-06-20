// package dev.rest;


// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Method;
// import java.util.Collection;
// import java.util.List;

// import javax.xml.bind.annotation.*;

// import com.opensymphony.workflow.loader.StepDescriptor;
// import com.opensymphony.workflow.loader.ActionDescriptor;
// import com.opensymphony.workflow.loader.FunctionDescriptor;

// @XmlRootElement(name = "WorkflowActionHandlerSearchEntity")
// @XmlAccessorType(XmlAccessType.FIELD)
// public class WorkflowActionHandlerSearchEntity {

//     @XmlElement(name = "id")
//     private int id;

//     @XmlElement(name = "entityId")
//     private int entityId;

//     @XmlElement(name = "name")
//     private String name;

//     @XmlElement(name = "type")
//     public String type;

//     @XmlElement(name = "order")
//     public int order;

//     @XmlElement(name = "class name")
//     public String className;

//     @XmlElement(name = "isCommon")
//     public boolean isCommon;

//     @XmlElement(name = "asXML")
//     public String asXML;

//     @XmlElement(name = "transition")
//     public transition transition;



//     public WorkflowActionHandlerSearchEntity(){

//     }

//     public WorkflowActionHandlerSearchEntity(StepDescriptor descriptor, String type, ActionDescriptor transition){
//         this.id = descriptor.getId();
//         this.entityId = descriptor.getEntityId();
//         this.name = descriptor.getName();
//         this.type = type;
//         this.className = descriptor.getClass().getName();
//         this.asXML = descriptor.asXML();

//         this.transition = new EntityTransition(transition);

//     }


// }
