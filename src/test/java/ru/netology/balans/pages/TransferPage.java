package ru.netology.balans.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    // перевод с указанной карты
    public void transfer(String amount, ru.netology.balans.data.DataHelper.Card fromCard) {
        amountField.setValue(amount);
        fromField.setValue(fromCard.getNumber());
        transferButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
}