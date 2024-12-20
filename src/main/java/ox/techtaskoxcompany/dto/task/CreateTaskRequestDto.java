package ox.techtaskoxcompany.dto.task;

import java.time.LocalDate;
import lombok.Data;
import lombok.experimental.Accessors;
import ox.techtaskoxcompany.model.Task;

@Data
@Accessors(chain = true)
public class CreateTaskRequestDto {
    private String description;
    private Task.Status status;
    private LocalDate deadline;
    private Long contactId;
}
