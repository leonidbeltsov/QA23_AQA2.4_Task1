package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {

    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cardList = $$(".list .list__item");
    private final SelenideElement fieldAmount = $("[data-test-id=amount] input");
    private final SelenideElement fieldFrom = $("[data-test-id=from] input");
    private final SelenideElement fieldTo = $("[data-test-id=to] input");
    private final SelenideElement buttonAction = $("[data-test-id=action-transfer]");
    private final SelenideElement buttonActionReload = $("[data-test-id=action-reload]");

    public DashboardPage() {
        heading.shouldBe(visible, Duration.ofSeconds(5));
    }

    private void openTransferForm(String cardNumber) {
        String lastFourDigit = cardNumber.substring(cardNumber.length() - 4);
        cardList.findBy(Condition.text(lastFourDigit)).$(withText("Пополнить")).click();
        $(withText("Пополнение карты")).shouldBe(visible);
    }

    private void setAmountValue(int amount) {
        fieldAmount.clear();
        fieldAmount.setValue(String.valueOf(amount));
    }

    private void setFromValue(String cardNumber) {
        fieldFrom.setValue(cardNumber);
    }

    private void doTransfer() {
        fieldTo.shouldBe(disabled);
        buttonAction.click();
    }

    public void updateBalance() {
        heading.shouldBe(visible, Duration.ofSeconds(5));
        buttonActionReload.click();
    }

    public int getBalance(String cardNumber) {
        String lastFourDigit = cardNumber.substring(cardNumber.length() - 4);
        String[] cardInfo = cardList.findBy(Condition.text(lastFourDigit)).getText().split(" ");
        return Integer.parseInt(cardInfo[5]);
    }

    public void moneyTransfer(String cardNumberFrom, String cardNumberTo, int amount) {
        openTransferForm(cardNumberTo);
        setAmountValue(amount);
        setFromValue(cardNumberFrom);
        doTransfer();
    }
}