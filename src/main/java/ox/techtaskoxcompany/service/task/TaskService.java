package ox.techtaskoxcompany.service.task;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;

public interface TaskService {
    TaskDto save(CreateTaskRequestDto requestDto);

    List<TaskDto> getAll(Pageable pageable);

    TaskDto findById(Long id);

    void deleteById(Long id);

    TaskDto updateById(Long id, CreateTaskRequestDto requestDto);

    TaskDto updateContactById(Long id, CreateTaskRequestDto requestDto);

    TaskDto updateTaskStatusById(Long id, CreateTaskRequestDto requestDto);
}
