package ox.techtaskoxcompany.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE tasks SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Table(name = "tasks")
public class Task {
    public enum Status {
        OPEN,
        IN_PROGRESS,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private LocalDate deadline;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Contact contact;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
