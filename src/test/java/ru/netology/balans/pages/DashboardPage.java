package ru.netology.balans.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.balans.data.DataHelper;
import ru.netology.balans.data.DataHelper.Card;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Condition.text;

public class DashboardPage {

    private ElementsCollection cards = $$(".list__item");

    public int getCardBalance(Card card) {
        String masked = DataHelper.getMaskedNumber(card);
        SelenideElement currentCard = cards.findBy(text(masked));
        String fullText = currentCard.getText();
        return extractBalance(fullText);
    }

    public TransferPage selectCardToDeposit(Card card) {
        String masked = DataHelper.getMaskedNumber(card);

        cards.findBy(text(masked))
                .$("[data-test-id=action-deposit]")
                .click();
        return new TransferPage();
    }

    public int extractBalance(String text) {
        String balanceStart = "баланс: ";
        String balanceFinish = " р.";

        int start = text.indexOf(balanceStart) + balanceStart.length();
        int finish = text.indexOf(balanceFinish);

        String value = text.substring(start, finish).trim(); // "-205000"
        return Integer.parseInt(value);
    }
}