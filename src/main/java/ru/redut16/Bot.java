package ru.redut16;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.sql.*;
import java.time.*;
import java.util.Map;
import static ru.redut16.StaticVariable.*;


public class Bot extends TelegramLongPollingBot {

    private Connection connection;
    private String passportNumber = null;
    private String dateBirthDay = null;
    private int actionPressed;
    private String userID = null;
    private OffsetDateTime dateMessage = null;

    {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final Map<String, UpdateHandler> handlers = Map.of(
            "GetDuty", this::handleGetDuty,
            "Pay", this::handlePay,
            "GetPaper", this::handleGetPaper,
            "PayDiscount", this::handlePayDiscount,
            "CallBack", this::handleCallBack,
            "FeedBack", this::handleFeedBack,
            "KnowContract", this::handleKnowContract,
            "DontKnowContract", this::handleDontKnowContract,
            "callBackTomorrow", this::handleCallBackTomorrow,
            "callBackAfterTomorrow", this::handleCallBackAfterTomorrow

    );

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            handleText(update);
        } else if (update.hasCallbackQuery()) {
            handleButtonClick(update);
        }


    }

    private void handleButtonClick(Update update) throws TelegramApiException {
        CallbackQuery callbackquery = update.getCallbackQuery();
        String data = callbackquery.getData();
        UpdateHandler handler = handlers.get(data);
        handler.handle(update);
    }

    private void handleText(Update update) throws TelegramApiException {

        if (update.getMessage().getText().equals("/start")) {
            userID = update.getMessage().getChatId().toString();
            execute(StartFirstInlineKeyboard.StartInlineKeyboard(userID));
        }


            //                клиент ввел серию и номер паспорта, проверяем
            if (update.getMessage().getText().matches("\\d{4}[#]\\d{6}")) {
                String text = update.getMessage().getText();
                String input = text.replaceAll("[#]", "");
                System.out.println("UserId: " + userID +  " ввел серия#номер паспорта: " + text);
                execute(SendMessage.builder().chatId(userID).text("проверяю, ожидайте....").build());
                APIpassNum apiPassNum = new APIpassNum();
                try {
                    String passport = apiPassNum.QueryPassNum(input, userID);
                    if (passport.equals("1")){
                        passportNumber = input;
                        execute(SendMessage.builder().chatId(userID).text("Введите дату рождения, в формате: дд.мм.гггг(например: 01.01.2001)").build());
                    }else if (passport.equals("2")){
                        execute(SendMessage.builder().chatId(userID).text("возможно, Вы меняли паспорт, попробуйте ввести прежний паспорт серия#номер (например 1234#123456)").build());
                    }
                } catch (Exception e) {
                    execute(SendMessage.builder().chatId(userID).text("сервис временно не доступен, попробуйте позднее").build());
                    System.out.println("Exception API 1c passNum = " + e);
                }
            }

            //                клиент ввел дату рождения, проверяем
            if (update.getMessage().getText().matches("\\d{2}[.]\\d{2}[.]\\d{4}")) {
                String inputBirthday = update.getMessage().getText().replaceAll("[.]", "");
                System.out.println("UserId: " + userID + " ввел Дату рождения: " + inputBirthday);
                execute(SendMessage.builder().chatId(userID).text("проверяю, ожидайте....").build());
                APIdateBirth apIdateBirth = new APIdateBirth();
                try {
                    String dateBirth = apIdateBirth.QuerydateBirth(inputBirthday, passportNumber, userID);
                    if (dateBirth.equals("1")){
                        dateBirthDay = inputBirthday;
                        execute(SendMessage.builder().chatId(userID).text("Запрошенный Вами документ: ").build());
                        System.out.println("UserId: " + userID + " начинаем отправку файла");
                        GetFile file = new GetFile();
                        if(actionPressed == 1){
                            execute(file.sendDocument(userID));
                            System.out.println("UserId: " + userID + " файл отправлен.");
                            try {sqlUpdateState(userID, actionPressed);} catch (Exception e) {e.printStackTrace();}
                        }
                        if(actionPressed == 3){
                            execute(file.sendDocument(userID));
                            System.out.println("UserId: " + userID + " файл отправлен.");
                            try {sqlUpdateState(userID, actionPressed);} catch (Exception e) {e.printStackTrace();}
                        }
                        if(actionPressed == 4){
                            execute(file.sendDocument(userID));
                            System.out.println("UserId: " + userID + " файл отправлен.");
                            try {sqlUpdateState(userID, actionPressed);} catch (Exception e) {e.printStackTrace();}
                        }
                    }else if(dateBirth.equals("2")){
                        execute(SendMessage.builder().chatId(userID).text("попробуйте еще раз ввести дату рождения, пример 01.01.2001").build());
                    }
                } catch (Exception e) {
                    execute(SendMessage.builder().chatId(userID).text("сервис временно не доступен, попробуйте позднее").build());
                    System.out.println("Exception API 1c dateBirthday = " + e);
                }
            }

    }


    public void handleGetDuty(Update update) throws TelegramApiException {
        actionPressed = 1;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed GetDuty");
        execute(SendMessage.builder().chatId(userID).text(PASSPORT_NEED).build());
    }

    public void handlePay(Update update) throws TelegramApiException {
        actionPressed = 2;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed Pay");
        execute(SendMessage.builder().chatId(userID).text("Вам известен номер договора?").build());
        execute(StartPayInlineKeyboard.StartInlineKeyboardPay(userID));
    }

    public void handleGetPaper(Update update) throws TelegramApiException {
        actionPressed = 3;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed GetPaper");
        execute(SendMessage.builder().chatId(userID).text(PASSPORT_NEED).build());
    }

    public void handlePayDiscount(Update update) throws TelegramApiException {
        actionPressed = 4;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed PayDiscount");
        execute(SendMessage.builder().chatId(userID).text(PASSPORT_NEED).build());
    }

    public void handleCallBack(Update update) throws TelegramApiException {
        actionPressed = 5;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed CallBack");
        InlineCalendar calendar = new InlineCalendar();
        execute(calendar.StartInlineCalendar(userID));
    }

    public void handleFeedBack(Update update) throws TelegramApiException {
        actionPressed = 6;
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed FeedBack");
        execute(SendMessage.builder().chatId(userID).text("Введите обращение. Обращение: далее ваше обращение").build());
    }

    public void handleKnowContract(Update update) throws TelegramApiException {
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed KnowContract");
        execute(SendMessage.builder().chatId(userID).text("Введите номер договора в формате: ###номер договора(вначале ###)").build());
    }

    public void handleDontKnowContract(Update update) throws TelegramApiException {
        try {sqlQuery(update, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed DontKnowContract");
        execute(SendMessage.builder().chatId(userID).text("В таком случае требуется авторизация. " + PASSPORT_NEED).build());
    }

    public void handleCallBackTomorrow(Update update) throws TelegramApiException {
        try {sqlUpdateState(userID, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed callBackTomorrow");
        execute(SendMessage.builder().chatId(userID).text("Мы Вам перезвоним в рабочее время с 8:00 до 17:00(Мск).").build());
    }

    public void handleCallBackAfterTomorrow(Update update) throws TelegramApiException {
        try {sqlUpdateState(userID, actionPressed);} catch (Exception e) {e.printStackTrace();}
        System.out.println("UserId: " + userID + " Pressed callBackAfterTomorrow");
        execute(SendMessage.builder().chatId(userID).text("Мы Вам перезвоним в рабочее время с 8:00 до 17:00(Мск).").build());
    }




    public void sqlQuery(Update update, int action) throws Exception {
        Long userId = update.getCallbackQuery().getMessage().getChatId();
        String userName = update.getCallbackQuery().getFrom().getUserName();

        Instant createdTime = Instant.ofEpochSecond(update.getCallbackQuery().getMessage().getDate());
        ZoneId zone = ZoneId.of("Z");
        OffsetDateTime date = OffsetDateTime.ofInstant(createdTime,zone);
        dateMessage = date;


                // language=PostgreSQL
        String SQL_INSERT = """
                INSERT INTO messages(user_id, user_name, created, action)
                VALUES(?, ?, ?, ?);""";

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT);
        preparedStatement.setLong(1, userId);
        preparedStatement.setString(2, userName);
        preparedStatement.setObject(3, date);
        preparedStatement.setInt(4, action);
        preparedStatement.executeUpdate();
    }


    public void sqlUpdateState(String userID, int actionPressed) throws Exception {

        // language=PostgreSQL
        String SQL_UPDATE = """
                UPDATE messages SET success = TRUE WHERE user_id = ? AND action = ? AND created = ?;
                """;

        PreparedStatement pS = connection.prepareStatement(SQL_UPDATE);
        pS.setLong(1, Long.parseLong(userID));
        pS.setInt(2, actionPressed);
        pS.setObject(3, dateMessage);
        pS.executeUpdate();
    }



    @Override
    public String getBotUsername() {
        return BOT_USER_NAME;
    }

    public Bot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Bot bot = new Bot(new DefaultBotOptions());
        TelegramBotsApi botApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botApi.registerBot(bot);
            System.out.println("Telegram Bot registered");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

}
