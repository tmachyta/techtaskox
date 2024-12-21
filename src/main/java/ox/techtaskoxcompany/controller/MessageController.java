package ox.techtaskoxcompany.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import ox.techtaskoxcompany.dto.message.Message;
import ox.techtaskoxcompany.dto.message.ResponseMessage;
import ox.techtaskoxcompany.service.notification.NotificationService;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final NotificationService notificationService;

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ResponseMessage getMessage(Message message) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification();
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getContent()));
    }
}
