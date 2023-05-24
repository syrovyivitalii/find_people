package bot.findme.service;

import bot.findme.model.Keyboard;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@Component
public class ReplyKeyboard {
    private ReplyKeyboardMarkup createKeyboard(List<Keyboard> keyboards) {
        // Створюємо клавіатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> rows = new ArrayList<>();

        for (Keyboard keyboard : keyboards) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton(keyboard.getKeyboard());
            row.add(keyboardButton);
            rows.add(row);
        }

        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getCreateKeyboard (List<Keyboard> keyboards){
        return createKeyboard(keyboards);
    }

}
