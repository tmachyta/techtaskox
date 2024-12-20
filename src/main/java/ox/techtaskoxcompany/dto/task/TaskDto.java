package ox.techtaskoxcompany.dto.task;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import ox.techtaskoxcompany.model.Task.Status;

@Data
@Accessors(chain = true)
public class TaskDto {
    private Long id;
    private String description;
    private Status status;
    private LocalDate deadline;
    private Long contactId;
}
