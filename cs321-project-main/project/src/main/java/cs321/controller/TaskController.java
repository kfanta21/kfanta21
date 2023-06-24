package cs321.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cs321.repository.TaskRepository;
import cs321.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/createTask")
    public ResponseEntity<String> createTask(@RequestBody Map<String, String> taskData, HttpServletRequest request) {
        // Retrieve the task data from the request
        String taskName = taskData.get("taskName");
        String dueDate = taskData.get("dueDate");

        // Get the username from the session
        String username = (String) request.getSession().getAttribute("username");

        // Create a new task
        Task task = new Task();
        task.setTaskName(taskName);
        task.setDueDate(dueDate);
        task.setUsername(username);

        // Save the task to the database
        taskRepository.save(task);

        // Return a response
        return new ResponseEntity<>("Task created successfully", HttpStatus.OK);
    }
}
