package ox.techtaskoxcompany.service.task;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;
import ox.techtaskoxcompany.exception.EntityNotFoundException;
import ox.techtaskoxcompany.mapper.task.TaskMapper;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.model.Task;
import ox.techtaskoxcompany.repository.contact.ContactRepository;
import ox.techtaskoxcompany.repository.task.TaskRepository;
import ox.techtaskoxcompany.service.contact.ContactServiceImpl;
import ox.techtaskoxcompany.service.notification.NotificationService;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ContactRepository contactRepository;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public TaskDto save(CreateTaskRequestDto requestDto) {
        Contact contact = contactRepository.findById(requestDto.getContactId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id "
                        + requestDto.getContactId()));
        logger.debug("Found contact: {}", contact);
        Task task = taskMapper.toModel(requestDto);
        task.setContact(contact);
        logger.info("Saving new task with data: {}", requestDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> getAll(Pageable pageable) {
        logger.info("Getting list of tasks");
        return taskRepository.findAll(pageable)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found task: {}", task);
        return taskMapper.toDto(task);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleted task");
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto updateById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found task: {}", existedTask);
        existedTask.setDescription(requestDto.getDescription());
        existedTask.setDeadline(requestDto.getDeadline());
        logger.info("Saving updated task with data: {}", existedTask);
        return taskMapper.toDto(taskRepository.save(existedTask));
    }

    @Override
    public TaskDto updateContactById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found task: {}", existedTask);

        Contact contact = contactRepository.findById(requestDto.getContactId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id "
                        + requestDto.getContactId()));
        logger.debug("Found contact: {}", contact);

        existedTask.setContact(contact);
        notificationService.sendNotificationAboutContact(existedTask.getId(), contact.getId());
        return taskMapper.toDto(taskRepository.save(existedTask));
    }

    @Override
    public TaskDto updateTaskStatusById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found task: {}", existedTask);
        existedTask.setStatus(requestDto.getStatus());
        notificationService.sendNotificationAboutStatus(existedTask.getId(),
                requestDto.getStatus());
        return taskMapper.toDto(taskRepository.save(existedTask));
    }
}
