package ox.techtaskoxcompany.service.task;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;
import ox.techtaskoxcompany.exception.EntityNotFoundException;
import ox.techtaskoxcompany.mapper.task.TaskMapper;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.model.Task;
import ox.techtaskoxcompany.model.Task.Status;
import ox.techtaskoxcompany.repository.contact.ContactRepository;
import ox.techtaskoxcompany.repository.task.TaskRepository;
import ox.techtaskoxcompany.service.notification.NotificationService;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    private static final Long TASK_ID = 1L;
    private static final Long CONTACT_ID = 2L;
    private static final Long NON_EXISTED_TASK_ID = 50L;
    private static final String DESCRIPTION = "Old";
    private static final String NEW_DESCRIPTION = "Updated";
    private static final Status OLD_STATUS = Status.OPEN;
    private static final Status NEW_STATUS = Status.IN_PROGRESS;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testSuccessfullySaveTask() {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setContactId(CONTACT_ID);

        Contact contact = new Contact();
        Task taskToSave = new Task();
        contact.setId(TASK_ID);

        when(contactRepository.findById(CONTACT_ID)).thenReturn(Optional.of(contact));

        when(taskMapper.toModel(requestDto)).thenReturn(taskToSave);

        taskToSave.setContact(contact);

        when(taskRepository.save(taskToSave)).thenReturn(taskToSave);

        TaskDto taskDto = new TaskDto();

        when(taskMapper.toDto(taskToSave)).thenReturn(taskDto);

        TaskDto result = taskService.save(requestDto);

        assertNotNull(result);
    }

    @Test
    public void testGetAllTasks() {
        Task task = new Task();
        Pageable pageable = PageRequest.of(0, 10);
        List<Task> tasks = List.of(new Task());
        List<TaskDto> expectedTasks = List.of(new TaskDto());
        Page<Task> page =
                new PageImpl<>(tasks, pageable, tasks.size());

        when(taskRepository.findAll(pageable)).thenReturn(page);

        when(taskMapper.toDto(task)).thenReturn(new TaskDto());

        List<TaskDto> result = taskService.getAll(pageable);

        Assertions.assertEquals(expectedTasks.size(), result.size());
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();
        task.setId(TASK_ID);
        TaskDto taskDto = new TaskDto();
        taskDto.setId(TASK_ID);

        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.findById(TASK_ID);

        Assertions.assertEquals(task.getId(), result.getId());
    }

    @Test
    public void testFindTaskWithNonExistedId() {
        when(taskRepository.findById(NON_EXISTED_TASK_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> taskService.findById(NON_EXISTED_TASK_ID));
    }

    @Test
    public void testDeleteTaskById() {
        taskService.deleteById(TASK_ID);

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> taskService.findById(TASK_ID));
    }

    @Test
    public void testUpdateTaskSuccessfully() {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setDescription(NEW_DESCRIPTION);

        TaskDto expectedResult = new TaskDto();
        expectedResult.setDescription(NEW_DESCRIPTION);

        Task task = new Task();
        task.setDescription(DESCRIPTION);

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);

        when(taskMapper.toDto(task)).thenReturn(expectedResult);

        TaskDto updatedTask =
                taskService.updateById(TASK_ID, requestDto);

        Assertions.assertEquals(updatedTask.getDescription(), expectedResult.getDescription());
    }

    @Test
    public void testUpdateContactId() {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setContactId(CONTACT_ID);

        Task task = new Task();
        task.setId(TASK_ID);

        Contact contact = new Contact();
        contact.setId(CONTACT_ID);

        when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

        when(contactRepository.findById(CONTACT_ID)).thenReturn(Optional.of(contact));
        task.setContact(contact);

        when(taskRepository.save(task)).thenReturn(task);

        TaskDto expectedResult = new TaskDto();
        expectedResult.setId(TASK_ID);
        expectedResult.setContactId(CONTACT_ID);
        when(taskMapper.toDto(task)).thenReturn(expectedResult);

        TaskDto updatedTask = taskService.updateContactById(TASK_ID, requestDto);

        Assertions.assertEquals(updatedTask.getContactId(), expectedResult.getContactId());
    }

    @Test
    public void testUpdateTaskStatusById() {
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto();
        requestDto.setStatus(NEW_STATUS);

        TaskDto expectedResult = new TaskDto();
        expectedResult.setStatus(NEW_STATUS);

        Task task = new Task();
        task.setStatus(OLD_STATUS);

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task)).thenReturn(task);

        when(taskMapper.toDto(task)).thenReturn(expectedResult);

        TaskDto updatedTask =
                taskService.updateTaskStatusById(TASK_ID, requestDto);

        Assertions.assertEquals(updatedTask.getStatus(), expectedResult.getStatus());
    }
}
