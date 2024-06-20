package com.example.workflow;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vacation")
public class VacationController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/request-vacation/{employeeId}")
    public ResponseEntity<String> requestVacation(@PathVariable("employeeId") String employeeId) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("employeeId", employeeId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-project-process", variables);
        return ResponseEntity.ok("Vacation request submitted, process instance id: " + processInstance.getId());
    }

    @GetMapping("/tasks/{assignee}")
    public ResponseEntity<List<TaskDto>> getTasks(@PathVariable("assignee") String assignee) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .initializeFormKeys()
                .list();
        List<TaskDto> taskDtos = tasks.stream()
                .map(task -> new TaskDto(task.getId(), task.getName(), task.getFormKey()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }

    @PostMapping("/complete-task/{taskId}")
    public ResponseEntity<String> completeTask(@PathVariable("taskId") String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            taskService.complete(taskId);
            return ResponseEntity.ok("Task with id " + taskId + " has been completed.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id " + taskId + " not found.");
        }
    }

    @PostMapping("/approve-by-manager/{taskId}")
    public ResponseEntity<String> approveByManager(@PathVariable("taskId") String taskId) {
        taskService.complete(taskId);
        return ResponseEntity.ok("Manager approved vacation request, task id: " + taskId);
    }


    @PostMapping("/approve-by-hr/{taskId}")
    public ResponseEntity<String> approveByHr(@PathVariable("taskId") String taskId) {
        taskService.complete(taskId);
        return ResponseEntity.ok("HR approved vacation request, task id: " + taskId);
    }
}

