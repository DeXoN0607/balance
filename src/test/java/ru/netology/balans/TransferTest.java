package ru.netology.balans;

import org.junit.jupiter.api.Test;
import ru.netology.balans.data.DataHelper;
import ru.netology.balans.data.DataHelper.Card;
import ru.netology.balans.pages.DashboardPage;
import ru.netology.balans.pages.LoginPage;
import ru.netology.balans.pages.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferTest extends BaseTest {

    @Test
    void shouldTransferMoneyBetweenCards() {

        var user = DataHelper.getAuthUser();

        DashboardPage dashboard = open("/", LoginPage.class)
                .validLogin(user)
                .validVerify(DataHelper.getVerificationCode());

        Card firstCard = DataHelper.getFirstCard();
        Card secondCard = DataHelper.getSecondCard();

        int amount = 1000;

        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);

        dashboard.selectCardToDeposit(firstCard)
                .transfer(String.valueOf(amount), secondCard);


        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore + amount, firstAfter);
        assertEquals(secondBefore - amount, secondAfter);
    }

    @Test
    void shouldNotTransferIfAmountIsTooBig() {

        var user = DataHelper.getAuthUser();

        DashboardPage dashboard = open("/", LoginPage.class)
                .validLogin(user)
                .validVerify(DataHelper.getVerificationCode());

        Card firstCard = DataHelper.getFirstCard();
        Card secondCard = DataHelper.getSecondCard();

        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);

        int amount = secondBefore + 5000; // больше, чем баланс второй карты

        dashboard.selectCardToDeposit(firstCard)
                .transfer(String.valueOf(amount), secondCard);

        // проверяем сообщение об ошибке
        TransferPage transferPage = new TransferPage();
        String error = transferPage.getErrorMessage();
        assertTrue(error.contains("Недостаточно денежных средств на карте"));

        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore, firstAfter);
        assertEquals(secondBefore, secondAfter);
    }
}