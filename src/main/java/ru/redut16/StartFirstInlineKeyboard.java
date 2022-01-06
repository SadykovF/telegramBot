package ru.redut16;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartFirstInlineKeyboard {
    public static SendMessage StartInlineKeyboard(String userChatId) {
        System.out.println("UserId: " + userChatId + " Pressed /start ");

        SendMessage sendMessage = new SendMessage();

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Узнать задолженность");
        inlineKeyboardButton1.setCallbackData("GetDuty");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Оплатить задолженность");
        inlineKeyboardButton2.setCallbackData("Pay");

        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("Получить справку");
        inlineKeyboardButton3.setCallbackData("GetPaper");

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText("Погасить со скидкой");
        inlineKeyboardButton4.setCallbackData("PayDiscount");

        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        inlineKeyboardButton5.setText("Заказать звонок");
        inlineKeyboardButton5.setCallbackData("CallBack");

        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        inlineKeyboardButton6.setText("Написать обращение");
        inlineKeyboardButton6.setCallbackData("FeedBack");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(inlineKeyboardButton3);
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();
        keyboardButtonsRow4.add(inlineKeyboardButton4);
        List<InlineKeyboardButton> keyboardButtonsRow5 = new ArrayList<>();
        keyboardButtonsRow5.add(inlineKeyboardButton5);
        List<InlineKeyboardButton> keyboardButtonsRow6 = new ArrayList<>();
        keyboardButtonsRow6.add(inlineKeyboardButton6);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        rowList.add(keyboardButtonsRow5);
        rowList.add(keyboardButtonsRow6);

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText("Выберите один из вариантов: ");
        sendMessage.setChatId(userChatId);

        try {
            return sendMessage;
        } catch (Exception e) {
            System.out.println("Exception in FIRST InlineKeyBoard = " + e);
        }

        return null;

    }
}
