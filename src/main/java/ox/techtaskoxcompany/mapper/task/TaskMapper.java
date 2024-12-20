package ox.techtaskoxcompany.mapper.task;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ox.techtaskoxcompany.config.MapperConfig;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;
import ox.techtaskoxcompany.model.Task;

@Mapper(config = MapperConfig.class)
public interface TaskMapper {

    @Mapping(source = "contact.id", target = "contactId")
    TaskDto toDto(Task task);

    @Mapping(target = "id", ignore = true)
    Task toModel(CreateTaskRequestDto requestDto);
}
