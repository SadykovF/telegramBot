package ru.redut16;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class StartPayInlineKeyboard {
    public static SendMessage StartInlineKeyboardPay(String userChatId) {
        SendMessage sendMessagePay = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Я знаю номер договора");
        inlineKeyboardButton1.setCallbackData("KnowContract");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("Я НЕ знаю номер договора");
        inlineKeyboardButton2.setCallbackData("DontKnowContract");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineKeyboardButton2);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessagePay.setReplyMarkup(inlineKeyboardMarkup);
        sendMessagePay.setText("Выберите один из вариантов: ");
        sendMessagePay.setChatId(userChatId);

        try {
            return sendMessagePay;
        } catch (Exception e) {
            System.out.println("Exception in PAY InlineKeyBoard = " + e);
        }

        return null;
    }
}
