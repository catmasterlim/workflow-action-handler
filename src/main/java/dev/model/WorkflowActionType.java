package dev.model;

public enum WorkflowActionType {
    Condition("Condition"),
    Validator("Validator"),
    PostFunction("PostFunction")
    ;

    private final String lable;

    WorkflowActionType(String label){
        this.lable = label;
    }

    public String label() {
        return this.lable;
    }

}
