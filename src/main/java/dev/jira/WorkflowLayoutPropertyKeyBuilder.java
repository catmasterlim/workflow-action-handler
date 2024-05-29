 package dev.jira;
 
 import org.apache.commons.codec.digest.DigestUtils;
 import org.apache.commons.lang3.StringUtils;
 
 
 public abstract class WorkflowLayoutPropertyKeyBuilder
 {
   private String workflowName;
   private WorkflowState workflowState;
   private boolean version2;
   
   protected WorkflowState getWorkflowState() {
     return this.workflowState;
   }
 
   
   protected String getWorkflowName() {
     return this.workflowName;
   }
 
   
   public static WorkflowLayoutPropertyKeyBuilder newBuilder() {
     return new MD5();
   }
 
   
   public WorkflowLayoutPropertyKeyBuilder setWorkflowName(String workflowName) {
     this.workflowName = workflowName;
     return this;
   }
 
   
   public WorkflowLayoutPropertyKeyBuilder setWorkflowState(WorkflowState workflowState) {
     this.workflowState = workflowState;
     return this;
   }
 
   
   public WorkflowLayoutPropertyKeyBuilder setVersion2(boolean version2) {
     this.version2 = version2;
     return this;
   }
 
   
   public boolean isVersion2() {
     return this.version2;
   }
   
   public abstract String build();
   
   private static class MD5
     extends WorkflowLayoutPropertyKeyBuilder {
     private MD5() {}
     
     public String build() {
       StringBuilder prefix = new StringBuilder(getWorkflowState().keyPrefix());
       if (isVersion2())
       {
         prefix.append(".v5");
       }
       return StringUtils.join((Object[])new String[] { prefix.append(":").toString() + DigestUtils.md5Hex(getWorkflowName()) });
     }
   }
   
   public enum WorkflowState
   {
     LIVE
     {
       
       String keyPrefix()
       {
         return "jira.workflow.layout";
       }
     },
     DRAFT
     {
       
       String keyPrefix()
       {
         return "jira.workflow.draft.layout";
       }
     };
     
     abstract String keyPrefix();
   }
 }
