package bot.findme.messagesender;

import bot.findme.service.FindMeBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class MessageSenderImpl implements MessageSender {
    private FindMeBot findMeBot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            findMeBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setFindMeBot(FindMeBot findMeBot) {
        this.findMeBot = findMeBot;
    }
}
