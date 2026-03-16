package ru.netology.balans;

import org.junit.jupiter.api.Test;
import ru.netology.balans.data.DataHelper;
import ru.netology.balans.pages.DashboardPage;
import ru.netology.balans.pages.LoginPage;
import ru.netology.balans.pages.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransferTest extends BaseTest {

    @Test
    void shouldTransferMoneyBetweenCardsUsingActualBalances() {
        LoginPage loginPage = open("/", LoginPage.class);
        DashboardPage dashboard = loginPage
                .validLogin(DataHelper.getLogin(), DataHelper.getPassword())
                .validVerify(DataHelper.getVerificationCode());

        int firstBalanceBefore = dashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceBefore = dashboard.getCardBalance(DataHelper.getSecondCardId());

        int transferAmount = Math.min(1000, secondBalanceBefore);

        TransferPage transferPage = dashboard.selectCardToDeposit(DataHelper.getFirstCardId());
        transferPage.transfer(String.valueOf(transferAmount), DataHelper.getSecondCardNumber());

        DashboardPage updatedDashboard = new DashboardPage();

        int firstBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getSecondCardId());

        assertEquals(firstBalanceBefore + transferAmount, firstBalanceAfter,
                "Баланс первой карты после перевода некорректен");
        assertEquals(secondBalanceBefore - transferAmount, secondBalanceAfter,
                "Баланс второй карты после перевода некорректен");
    }

    @Test
    void shouldTransferMoneyFromSecondToFirstCard() {
        LoginPage loginPage = open("/", LoginPage.class);
        DashboardPage dashboard = loginPage
                .validLogin(DataHelper.getLogin(), DataHelper.getPassword())
                .validVerify(DataHelper.getVerificationCode());

        int firstBalanceBefore = dashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceBefore = dashboard.getCardBalance(DataHelper.getSecondCardId());

        int transferAmount = Math.min(2000, secondBalanceBefore);

        TransferPage transferPage = dashboard.selectCardToDeposit(DataHelper.getSecondCardId());
        transferPage.transfer(String.valueOf(transferAmount), DataHelper.getFirstCardNumber());

        DashboardPage updatedDashboard = new DashboardPage();

        int firstBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getSecondCardId());

        assertEquals(firstBalanceBefore + transferAmount, firstBalanceAfter,
                "Баланс первой карты после обратного перевода некорректен");
        assertEquals(secondBalanceBefore - transferAmount, secondBalanceAfter,
                "Баланс второй карты после обратного перевода некорректен");
    }

    @Test
    void shouldFailWhenTransferringMoreThanAvailable() {
        LoginPage loginPage = open("/", LoginPage.class);
        DashboardPage dashboard = loginPage
                .validLogin(DataHelper.getLogin(), DataHelper.getPassword())
                .validVerify(DataHelper.getVerificationCode());

        int firstBalanceBefore = dashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceBefore = dashboard.getCardBalance(DataHelper.getSecondCardId());

        // Намеренно берем сумму больше доступного баланса второй карты
        int transferAmount = secondBalanceBefore + 5000;

        TransferPage transferPage = dashboard.selectCardToDeposit(DataHelper.getFirstCardId());
        transferPage.transfer(String.valueOf(transferAmount), DataHelper.getSecondCardNumber());

        DashboardPage updatedDashboard = new DashboardPage();

        int firstBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getFirstCardId());
        int secondBalanceAfter = updatedDashboard.getCardBalance(DataHelper.getSecondCardId());

        // Этот тест должен упасть, показывая баг, что система позволила перевести больше, чем есть
        assertEquals(firstBalanceBefore + transferAmount, firstBalanceAfter,
                "Баланс первой карты после небезопасного перевода некорректен");
        assertEquals(secondBalanceBefore - transferAmount, secondBalanceAfter,
                "Баланс второй карты после небезопасного перевода некорректен");
    }
}