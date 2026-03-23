package ru.netology.balans.data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {}

    // Пользователь
    @Value
    public static class User {
        private String login;
        private String password;
    }

    // Карта
    @Value
    public static class Card {
        private String number;
    }

    // Получить пользователя для авторизации
    public static User getAuthUser() {
        return new User("vasya", "qwerty123");
    }

    // Получить проверочный код
    public static String getVerificationCode() {
        return "12345";
    }

    // Получить первую карту
    public static Card getFirstCard() {
        return new Card("5559 0000 0000 0001");
    }

    // Получить вторую карту
    public static Card getSecondCard() {
        return new Card("5559 0000 0000 0002");
    }

    // Маскированный номер карты
    public static String getMaskedNumber(Card card) {
        String last4 = card.getNumber().substring(card.getNumber().length() - 4);
        return "**** **** **** " + last4;
    }
}