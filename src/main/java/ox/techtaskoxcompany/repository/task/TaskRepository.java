package ox.techtaskoxcompany.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ox.techtaskoxcompany.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
