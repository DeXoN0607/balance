package ru.netology.balans;

import org.junit.jupiter.api.Test;
import ru.netology.balans.data.DataHelper;
import ru.netology.balans.data.DataHelper.Card;
import ru.netology.balans.pages.DashboardPage;
import ru.netology.balans.pages.LoginPage;
import ru.netology.balans.pages.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest extends BaseTest {

    @Test
    void shouldTransferMoneyBetweenCards() {
        var user = DataHelper.getAuthUser();

        DashboardPage dashboard = open("/", LoginPage.class)
                .validLogin(user)
                .validVerify(DataHelper.getVerificationCode());

        Card firstCard = DataHelper.getFirstCard();
        Card secondCard = DataHelper.getSecondCard();

        int firstBefore = dashboard.getCardBalance(firstCard);
        int secondBefore = dashboard.getCardBalance(secondCard);
        int amount = secondBefore/2;
        TransferPage transferPage = dashboard.selectCardToDeposit(firstCard);
        transferPage.transfer(String.valueOf(amount), secondCard);


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

        int amount = secondBefore + 5000;

        TransferPage transferPage = dashboard.selectCardToDeposit(firstCard);
        transferPage.transfer(String.valueOf(amount), secondCard);

        transferPage.checkErrorMessage("Недостаточно денежных средств на карте");

        int firstAfter = dashboard.getCardBalance(firstCard);
        int secondAfter = dashboard.getCardBalance(secondCard);

        assertEquals(firstBefore, firstAfter);
        assertEquals(secondBefore, secondAfter);
    }
}