package ru.redut16;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.*;
import static ru.redut16.StaticVariable.*;

public class InlineCalendar {
    public SendMessage StartInlineCalendar(String userChatId) {
        String dayMonthTomorrow = "";
        String dayMonthAfterTomorrow = "";

        System.out.println("UserId: " + userChatId + " We show Calendar to choose dayCallBack ");

//        Calendar cal = new GregorianCalendar(2021,10, 28);
//        Calendar cal = new GregorianCalendar(2021,0, 29);
//        Calendar cal = new GregorianCalendar(2021,3, 28);

        Calendar cal = new GregorianCalendar();

        Calendar calTomorrow = new GregorianCalendar();
        Calendar calAfterTomorrow = new GregorianCalendar();

        String knowMonthTomorrow = null;
        String knowMonthAfterTomorrow = null;

// ДНИ
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Integer dayTomorrow = cal.get(cal.DAY_OF_MONTH); // Tomorrow
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Integer dayAfterTomorrow = cal.get(cal.DAY_OF_MONTH); // AfterTomorrow


        if (dayTomorrow < 4){
            calTomorrow.add(calTomorrow.MONTH, 1);
            int nextMonth = calTomorrow.get(calTomorrow.MONTH);
            knowMonthTomorrow = theMonth(nextMonth);
        }else{
            int nextMonth = calTomorrow.get(calTomorrow.MONTH);
            knowMonthTomorrow = theMonth(nextMonth);
        }

        if (dayAfterTomorrow < 4){
            calAfterTomorrow.add(calAfterTomorrow.MONTH, 1);
            int nextMonth = calAfterTomorrow.get(calAfterTomorrow.MONTH);
            knowMonthAfterTomorrow = theMonth(nextMonth);
        }else{
            int nextMonth = calAfterTomorrow.get(calAfterTomorrow.MONTH);
            knowMonthAfterTomorrow = theMonth(nextMonth);
        }


        dayMonthTomorrow = (dayTomorrow.toString()) + " " + knowMonthTomorrow;
        dayMonthAfterTomorrow = (dayAfterTomorrow.toString()) + " " + knowMonthAfterTomorrow;


        SendMessage sendCalendar = new SendMessage();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(dayMonthTomorrow);
        inlineKeyboardButton1.setCallbackData("callBackTomorrow");

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText(dayMonthAfterTomorrow);
        inlineKeyboardButton2.setCallbackData("callBackAfterTomorrow");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(inlineKeyboardButton2);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        sendCalendar.setReplyMarkup(inlineKeyboardMarkup);
        sendCalendar.setText("Выберите дату: ");

        sendCalendar.setChatId(userChatId);


        try {
            return sendCalendar;
        } catch (Exception e) {
            System.out.println("Exception in Calendar = " + e);
        }

        return null;

    }

    public static String theMonth(int month){
        String[] monthNames = {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};
        return monthNames[month];
    }


}
