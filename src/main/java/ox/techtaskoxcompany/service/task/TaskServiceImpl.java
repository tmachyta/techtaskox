package ox.techtaskoxcompany.service.task;

import java.util.List;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ContactRepository contactRepository;

    @Override
    public TaskDto save(CreateTaskRequestDto requestDto) {
        Contact contact = contactRepository.findById(requestDto.getContactId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id "
                        + requestDto.getContactId()));
        Task task = taskMapper.toModel(requestDto);
        task.setContact(contact);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        return taskMapper.toDto(task);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto updateById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        existedTask.setDescription(requestDto.getDescription());
        existedTask.setDeadline(requestDto.getDeadline());
        return taskMapper.toDto(taskRepository.save(existedTask));
    }

    @Override
    public TaskDto updateContactById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));

        Contact contact = contactRepository.findById(requestDto.getContactId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id "
                        + requestDto.getContactId()));

        existedTask.setContact(contact);
        return taskMapper.toDto(taskRepository.save(existedTask));
    }

    @Override
    public TaskDto updateTaskStatusById(Long id, CreateTaskRequestDto requestDto) {
        Task existedTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        existedTask.setStatus(requestDto.getStatus());
        return taskMapper.toDto(taskRepository.save(existedTask));
    }
}
