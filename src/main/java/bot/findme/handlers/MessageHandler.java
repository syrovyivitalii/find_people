package bot.findme.handlers;

import bot.findme.messagesender.MessageSender;
import bot.findme.model.Menu;
import bot.findme.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
@Slf4j
public class MessageHandler implements Handler<Message>{

    private final MessageSender messageSender;
    private final MenuRepository menuRepository;

    public MessageHandler(MessageSender messageSender, MenuRepository menuRepository) {
        this.messageSender = messageSender;
        this.menuRepository = menuRepository;
    }

    @Override
    public void choose(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setParseMode(ParseMode.HTML);
        if (message.hasText()){
            log.info("{}:{}", message.getChatId(), message.getText());
            if (message.getText().equals("/start")){
                Optional<Menu> byId = menuRepository.findById(1L);
                sendMessage.setText(byId.get().getMenu());
                messageSender.sendMessage(sendMessage);
            }
        }
    }
}
