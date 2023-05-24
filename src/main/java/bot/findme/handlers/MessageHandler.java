package bot.findme.handlers;

import bot.findme.messagesender.MessageSender;
import bot.findme.model.Keyboard;
import bot.findme.model.Menu;
import bot.findme.model.SubmitNotoriety;
import bot.findme.model.User;
import bot.findme.repository.KeyboardRepository;
import bot.findme.repository.MenuRepository;
import bot.findme.service.ReplyKeyboard;
import bot.findme.repository.SubmitNotorietyRepository;
import bot.findme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MessageHandler implements Handler<Message>{
    private final SubmitNotorietyRepository submitNotorietyRepository;
    private final UserRepository userRepository;

    private final MessageSender messageSender;
    private final MenuRepository menuRepository;
    private final KeyboardRepository keyboardRepository;
    private final ReplyKeyboard replyKeyboard;

    public MessageHandler(MessageSender messageSender, MenuRepository menuRepository, KeyboardRepository keyboardRepository, ReplyKeyboard replyKeyboard,
                          UserRepository userRepository,
                          SubmitNotorietyRepository submitNotorietyRepository) {
        this.messageSender = messageSender;
        this.menuRepository = menuRepository;
        this.keyboardRepository = keyboardRepository;
        this.replyKeyboard = replyKeyboard;
        this.userRepository = userRepository;
        this.submitNotorietyRepository = submitNotorietyRepository;
    }

    @Override
    public void choose(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setParseMode(ParseMode.HTML);

        String userId = String.valueOf(message.getChatId());
        String messageText = message.getText();

        User user = new User();
        SubmitNotoriety submitNotoriety = new SubmitNotoriety();

        Optional<User> byUserID = userRepository.findByUserID(userId);

        String phoneRegex = "^\\+380\\d{9}$";
        String passportRegex = "^\\d{9}$";
        String lettersRegex = "^[а-щА-ЩЬьЮюЯяЇїІіЄєҐґ]+$";
        //check user
        if (byUserID.isPresent()){
            user = byUserID.get();
        }else {
            user.setUser_id(String.valueOf(message.getChatId()));
            userRepository.save(user);
        }
        if (message.hasText()){
            log.info("{}:{}", message.getChatId(), message.getText());
            if (messageText.equals("/start")){
                userRepository.setCommand(null,userId);
                userRepository.setValue(null,userId);
                userRepository.setSetPassport(null,userId);

                Optional<Menu> byId = menuRepository.findById(1L);
                if (byId.isPresent()){
                    sendMessage.setText(byId.get().getMenu());
                    List<Keyboard> menuKeyboard = keyboardRepository.findByMenu("меню");
                    sendMessage.setReplyMarkup(replyKeyboard.getCreateKeyboard(menuKeyboard));
                    messageSender.sendMessage(sendMessage);
                }
            }else if (user.getValue() == null) {
                if (messageText.equals("Подання відомості про зниклу особу \uD83D\uDCCB")){
                    Optional<Menu> byId = menuRepository.findById(2L);
                    if (byId.isPresent()){
                        sendMessage.setText(byId.get().getMenu());
                        messageSender.sendMessage(sendMessage);
                        userRepository.setCommand(messageText,userId);
                        userRepository.setValue("passport",userId);
                    }
                }
            }else {
                if (user.getCommand().equals("Подання відомості про зниклу особу \uD83D\uDCCB")){
                    if (user.getValue().equals("passport")) {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(messageText);
                        if (byPassport.isPresent()){
                            Optional<Menu> byId = menuRepository.findById(8L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }else {
                            if (messageText.matches(passportRegex)){
                                submitNotoriety.setPassport(messageText);
                                submitNotorietyRepository.save(submitNotoriety);

                                user.setSet_passport(messageText);
                                userRepository.save(user);

                                Optional<Menu> byId = menuRepository.findById(3L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);
                                    userRepository.setValue("lastName", userId);
                                }

                            }else {
                                Optional<Menu> byId = menuRepository.findById(11L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);
                                }
                            }
                        }

                    } else if (user.getValue().equals("lastName")) {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(user.getSet_passport());
                        if (messageText.matches(lettersRegex)){
                            if (byPassport.isPresent()){
                                submitNotoriety = byPassport.get();
                                submitNotoriety.setLast_name(messageText);
                                submitNotorietyRepository.save(submitNotoriety);

                                Optional<Menu> byId = menuRepository.findById(4L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("firstName", userId);
                                }
                            }

                        }else {
                            Optional<Menu> byId = menuRepository.findById(12L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    } else if (user.getValue().equals("firstName")) {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(user.getSet_passport());
                        if (messageText.matches(lettersRegex)){
                            if (byPassport.isPresent()){
                                submitNotoriety = byPassport.get();
                                submitNotoriety.setFirst_name(messageText);
                                submitNotorietyRepository.save(submitNotoriety);

                                Optional<Menu> byId = menuRepository.findById(5L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("middleName", userId);
                                }
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(13L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    } else if (user.getValue().equals("middleName")) {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(user.getSet_passport());
                        if (messageText.matches(lettersRegex)){
                            if (byPassport.isPresent()){
                                submitNotoriety = byPassport.get();
                                submitNotoriety.setMiddle_name(messageText);
                                submitNotorietyRepository.save(submitNotoriety);

                                Optional<Menu> byId = menuRepository.findById(6L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("phone", userId);
                                }
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(14L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    } else if (user.getValue().equals("phone")) {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(user.getSet_passport());

                        if (messageText.matches(phoneRegex)){
                            if (byPassport.isPresent()){
                                submitNotoriety = byPassport.get();
                                submitNotoriety.setPhone(messageText);
                                submitNotorietyRepository.save(submitNotoriety);

                                Optional<Menu> byId = menuRepository.findById(7L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("info", userId);
                                }
                            }
                        } else {
                            Optional<Menu> byId = menuRepository.findById(10L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }

                    } else {
                        Optional<SubmitNotoriety> byPassport = submitNotorietyRepository.findByPassport(user.getSet_passport());
                        if (byPassport.isPresent()){
                            submitNotoriety = byPassport.get();
                            submitNotoriety.setInformation(messageText);
                            submitNotorietyRepository.save(submitNotoriety);

                            Optional<Menu> byId = menuRepository.findById(9L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    }
                }
            }
        }
    }
}
