package dev.model.customInterpreters;

import dev.model.WorkflowActionEntity;

/**
 * workflow action 내용을 class에 맞춰서 적절하게 해석(변경)
 */
public interface ActionInterpreter {
    boolean has(WorkflowActionEntity entity);
    WorkflowActionEntity interpreterEntity(WorkflowActionEntity entity);
}
