package ox.techtaskoxcompany.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;
import ox.techtaskoxcompany.service.task.TaskService;

@Tag(name = "Task management", description = "Endpoints for managing Task")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/tasks")
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Get a list of all available tasks")
    public List<TaskDto> findAll(@ParameterObject Pageable pageable) {
        return taskService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get task by id", description = "Get available task by id")
    public TaskDto getContactById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Save task to repository",
            description = "Save valid task to repository")
    public TaskDto createContact(@RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id",
            description = "Soft delete of available task by id")
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update task by id", description = "Update available task by id")
    public TaskDto updateById(@PathVariable Long id,
                                 @RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/status/{id}")
    @Operation(summary = "Update task status by id", description = "Update available status by id")
    public TaskDto updateTaskStatusById(@PathVariable Long id,
                                        @RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.updateTaskStatusById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/contact/{id}")
    @Operation(summary = "Update contact by id", description = "Update available contact by id")
    public TaskDto updateContactById(@PathVariable Long id,
                                     @RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.updateContactById(id, requestDto);
    }
}
