package bot.findme.handlers;

import bot.findme.messagesender.MessageSender;
import bot.findme.model.*;
import bot.findme.repository.*;
import bot.findme.service.ReplyKeyboard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private final FoundPeopleRepository foundPeopleRepository;

    public MessageHandler(MessageSender messageSender, MenuRepository menuRepository, KeyboardRepository keyboardRepository, ReplyKeyboard replyKeyboard,
                          UserRepository userRepository,
                          SubmitNotorietyRepository submitNotorietyRepository, FoundPeopleRepository foundPeopleRepository) {
        this.messageSender = messageSender;
        this.menuRepository = menuRepository;
        this.keyboardRepository = keyboardRepository;
        this.replyKeyboard = replyKeyboard;
        this.userRepository = userRepository;
        this.submitNotorietyRepository = submitNotorietyRepository;
        this.foundPeopleRepository = foundPeopleRepository;
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
        String formatPattern = "dd/MM/yyyy";
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
                user.setCommand(null);
                user.setValue(null);
                user.setSet_passport(null);
                user.setSet_first_name(null);
                user.setSet_last_name(null);
                user.setSet_middle_name(null);
                user.setSet_date_of_birth(null);
                user.setSet_city(null);
                userRepository.save(user);

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
                } else if (messageText.equals("Перегляд інформації щодо знайдених осіб \uD83D\uDC6B")) {
                    Optional<Menu> byId = menuRepository.findById(15L);
                    if (byId.isPresent()){
                        sendMessage.setText(byId.get().getMenu());
                        messageSender.sendMessage(sendMessage);
                        if (byUserID.isPresent()){
                            user.setCommand(messageText);
                            user.setValue("lastName");
                            userRepository.save(user);
                        }
                    }
                }
            }else {
                if (user.getCommand().equals("Подання відомості про зниклу особу \uD83D\uDCCB")){
                    if (user.getValue().equals("passport")) {
                        if (messageText.matches(passportRegex)){
                            submitNotoriety.setPassport(messageText);
                            submitNotorietyRepository.save(submitNotoriety);

                            user.setSet_passport(messageText);
                            userRepository.save(user);

                            Optional<Menu> byId = menuRepository.findById(6L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                                userRepository.setValue("phone", userId);
                            }

                        }else {
                            Optional<Menu> byId = menuRepository.findById(11L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
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

                                Optional<Menu> byId = menuRepository.findById(7L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("info", userId);
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

                                Optional<Menu> byId = menuRepository.findById(3L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);

                                    userRepository.setValue("lastName", userId);
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
                } else if (user.getCommand().equals("Перегляд інформації щодо знайдених осіб \uD83D\uDC6B")) {
                    if (user.getValue().equals("lastName")){
                        if (messageText.matches(lettersRegex)){
                            user.setSet_last_name(messageText);
                            userRepository.save(user);

                            Optional<Menu> byId = menuRepository.findById(16L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                                user.setValue("firstName");
                                userRepository.save(user);
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(12L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    } else if (user.getValue().equals("firstName")) {
                        if (messageText.matches(lettersRegex)){
                            user.setSet_first_name(messageText);
                            userRepository.save(user);

                            Optional<Menu> byId = menuRepository.findById(17L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                                user.setValue("middleName");
                                userRepository.save(user);
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(13L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    }else if (user.getValue().equals("middleName")) {
                        if (messageText.matches(lettersRegex)){
                            user.setSet_middle_name(messageText);
                            userRepository.save(user);

                            Optional<Menu> byId = menuRepository.findById(18L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                                user.setValue("dateOfBirth");
                                userRepository.save(user);
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(14L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    }else if (user.getValue().equals("dateOfBirth")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
                        try{
                            LocalDate dateOfBirth = LocalDate.parse(messageText,formatter);
                            user.setSet_date_of_birth(dateOfBirth);

                            Optional<Menu> byId = menuRepository.findById(19L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                                user.setValue("city");
                            }
                            userRepository.save(user);
                        }catch (DateTimeParseException e){
                            Optional<Menu> byId = menuRepository.findById(20L);
                            if (byId.isPresent()){
                                sendMessage.setText(byId.get().getMenu());
                                messageSender.sendMessage(sendMessage);
                            }
                        }
                    }else {
                        if (messageText.matches(lettersRegex)){
                            user.setSet_city(messageText);
                            userRepository.save(user);
                            List<String> foundPeople = foundPeopleRepository.findMatchingRows(user.getSet_first_name(),user.getSet_last_name(),user.getSet_middle_name(),user.getSet_date_of_birth(),user.getSet_city());
                            if (!foundPeople.isEmpty()){
                                for (int i = 0; i < foundPeople.size(); i++){
                                    String text = "<b>Особу було знайдено:</b>\n\n" +
                                            "\uD83D\uDC64 ПІП: " + user.getSet_last_name() + " " + user.getSet_first_name() + " " + user.getSet_middle_name() + "\n\n"+
                                            "\uD83D\uDCC5 Дата народження: " + user.getSet_date_of_birth() +"\n\n" +
                                            "\uD83C\uDFE0 Місце проживання " + user.getSet_city() + "\n\n" +
                                            "\uD83D\uDCDD Інформація про знайденого: " + foundPeople.get(i);
                                    sendMessage.setText(text);
                                    messageSender.sendMessage(sendMessage);
                                }
                            }else {
                                Optional<Menu> byId = menuRepository.findById(22L);
                                if (byId.isPresent()){
                                    sendMessage.setText(byId.get().getMenu());
                                    messageSender.sendMessage(sendMessage);
                                }
                            }
                        }else {
                            Optional<Menu> byId = menuRepository.findById(21L);
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
