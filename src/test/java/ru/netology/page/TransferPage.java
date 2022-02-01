package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private static final SelenideElement headingTransferPage = $("[data-test-id=dashboard]+[class*='heading']");
    private static final SelenideElement fieldAmount = $("[data-test-id=amount] input");
    private static final SelenideElement fieldFrom = $("[data-test-id=from] input");
    private static final SelenideElement fieldTo = $("[data-test-id=to] input");
    private static final SelenideElement buttonAction = $("[data-test-id=action-transfer]");

    public TransferPage() {
        headingTransferPage.shouldBe(visible, Duration.ofSeconds(5));
        headingTransferPage.shouldBe(Condition.text("Пополнение карты"));
    }

    static void setAmountValue(int amount) {
        fieldAmount.clear();
        fieldAmount.setValue(String.valueOf(amount));
    }

    static void setFromValue(String cardNumber) {
        fieldFrom.setValue(cardNumber);
    }

    static void doTransfer() {
        fieldTo.shouldBe(disabled);
        buttonAction.click();
    }
}