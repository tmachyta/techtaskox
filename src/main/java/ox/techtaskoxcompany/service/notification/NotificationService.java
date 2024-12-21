package ox.techtaskoxcompany.service.notification;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ox.techtaskoxcompany.dto.message.ResponseMessage;
import ox.techtaskoxcompany.model.Task;

@Service
@AllArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendGlobalNotification() {
        ResponseMessage message = new ResponseMessage("Global Notification");

        messagingTemplate.convertAndSend("/topic/global-notifications", message);
    }

    public void sendNotificationAboutStatus(Long taskId, Task.Status status) {
        ResponseMessage message = new ResponseMessage("Task ID " + taskId
                + " status updated to: "
                + status);

        messagingTemplate.convertAndSend("/topic/task-status", message);
    }

    public void sendNotificationAboutContact(Long taskId, Long contactId) {
        ResponseMessage message = new ResponseMessage("Task ID " + taskId
                + " contact updated to: "
                + contactId);

        messagingTemplate.convertAndSend("/topic/task-contact", message);
    }
}
