package ru.netology.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    private final String firstCardNumber = "5559 0000 0000 0001";
    private final String secondCardNumber = "5559 0000 0000 0002";
    int amount;

    @BeforeEach
    void setup() {
        Configuration.headless = true;
    }

    public static DashboardPage openDashboard() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        return verificationPage.validVerify(verificationCode);
    }

    @Test
    @DisplayName("0001 > 0002")
    public void shouldTransferFromCard0001toCard002() {
        amount = 777;
        DashboardPage page = openDashboard();
        page.updateBalance();
//        Баланс карты получателя
        int currentBalance = page.getBalance(secondCardNumber);
        int expected = currentBalance + amount;
//        Перевод стредств (карта отправителя, карта получателя, сумма)
        page.moneyTransfer(firstCardNumber, secondCardNumber, amount);
        page.updateBalance();
        int actual = page.getBalance(secondCardNumber);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("0002 > 0001")
    public void shouldTransferFromCard0002toCard001() {
        amount = 5000;
        DashboardPage page = openDashboard();
        page.updateBalance();
//        Баланс карты получателя
        int currentBalance = page.getBalance(firstCardNumber);
        int expected = currentBalance + amount;
//        Перевод стредств (карта отправителя, карта получателя, сумма)
        page.moneyTransfer(secondCardNumber, firstCardNumber, amount);
        page.updateBalance();
        int actual = page.getBalance(firstCardNumber);
        assertEquals(expected, actual);
    }
}