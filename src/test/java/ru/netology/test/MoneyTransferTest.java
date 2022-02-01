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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoneyTransferTest {
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
        int expectedRecipient = page.getBalance(DataHelper.getSecondCardNumber()) + amount;
//        Баланс карты отправителя
        int expectedSender = page.getBalance(DataHelper.getFirstCardNumber()) - amount;
//        Перевод стредств (карта отправителя, карта получателя, сумма)
        page.moneyTransfer(DataHelper.getFirstCardNumber(), DataHelper.getSecondCardNumber(), amount);
        page.updateBalance();
        assertEquals(expectedRecipient, page.getBalance(DataHelper.getSecondCardNumber()));
        assertEquals(expectedSender, page.getBalance(DataHelper.getFirstCardNumber()));
    }

    @Test
    @DisplayName("0002 > 0001")
    public void shouldTransferFromCard0002toCard001v2() {
        amount = 10;
        DashboardPage page = openDashboard();
        page.updateBalance();
        int expectedRecipient = page.getBalance(DataHelper.getFirstCardNumber()) + amount;
        int expectedSender = page.getBalance(DataHelper.getSecondCardNumber()) - amount;
        page.moneyTransfer(DataHelper.getSecondCardNumber(), DataHelper.getFirstCardNumber(), amount);
        page.updateBalance();
        assertEquals(expectedRecipient, page.getBalance(DataHelper.getFirstCardNumber()));
        assertEquals(expectedSender, page.getBalance(DataHelper.getSecondCardNumber()));
    }

    @Test
    @DisplayName("Balance cannot be negative")
    public void shouldNotTransferFromCard0002toCard001() {
        amount = 20001;
        DashboardPage page = openDashboard();
        page.updateBalance();
        page.moneyTransfer(DataHelper.getFirstCardNumber(), DataHelper.getSecondCardNumber(), amount);
        page.updateBalance();
        assertTrue(page.getBalance(DataHelper.getFirstCardNumber()) >= 0);

    }
}

//java -jar ./artifacts/app-ibank-build-for-testers.jar