package ru.netology.balans.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.text;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public int getCardBalance(String lastDigits) {
        SelenideElement card = cards.findBy(text(lastDigits));
        String text = card.getText();

        String balance = text
                .split("баланс: ")[1]
                .split("\n")[0] // берём только первую строку
                .replaceAll("[^0-9]", "") // оставляем только цифры
                .trim();

        return Integer.parseInt(balance);
    }

    public TransferPage selectCardToDeposit(String lastDigits) {
        cards.findBy(text(lastDigits))
                .$("[data-test-id=action-deposit]")
                .click();
        return new TransferPage();
    }
}