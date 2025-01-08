package test;


import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentForm;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTests {
    MainPage mainPage;
    PaymentForm paymentForm;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        mainPage = open("http://localhost:8080", MainPage.class);
    }


    @Test
    @DisplayName("По кнопке \"Купить\" открывается форма \"Оплата по карте\"")
    void shouldOpenPaymentFormByButtonBuy() {
        mainPage.openPaymentForm();
    }

    @Test
    @DisplayName("При заполнении формы \"Оплата по карте\" валидными данными всех полей и нажатии кнопки \"Продолжить\" появляется уведомление об успешной оплате")
    void happyPathWithPaymentCard() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(true), getYear(true), getName(true), getValidCvc());
        paymentForm.expectedSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getPayStatus());
    }

    @Test
    @DisplayName("При нажатии кнопки \"Продолжить\" формы \"Оплата по карте\" с пустыми полями, у каждого поля появляется подсказка \"Поле обязательно для заполнения\"")
    void shouldFailPayIfAllFieldsIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(null, null, null, null, null);
        paymentForm.expectedFieldMustBeFilled();
    }

    @Test
    @DisplayName("При заполнении поля номера карты данными заблокированной карты происходит отказ в оплате в форме \"Оплата по карте\"")
    void shouldPaymentDenyWithDeclinedCard() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getDeclinedCardNumber(), getMonth(true), getYear(true), getName(true), getValidCvc());
        assertAll(() -> paymentForm.expectedFailureNotification(),
                () -> assertEquals("DECLINED", SQLHelper.getPayStatus())
        );

    }

    @Test
    @DisplayName("При заполнении поля номера карты 16 символами происходит отказ в проведении операции")
    void shouldBeRejectIfRandom16Symbols() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getInvalidCardNumberFormat(false), getMonth(true), getYear(true), getName(true), getValidCvc());
        paymentForm.expectedFailureNotification();
    }

    @Test
    @DisplayName("Если ввести в поля количество символов, больше валидного, в полях остается валидное количество символов")
    void shouldBeTrimmed() {
        paymentForm = mainPage.openPaymentForm();
        String cardNumber = getInvalidCardNumberFormat(false);
        String month = getInvalidMonthFormat(false);
        String year = getInvalidYearFormat(false);
        String name = getInvalidNameFormat(false);
        String cvc = getInvalidCvc(false);
        paymentForm.fillCardData(cardNumber, month, year, name, cvc);
        String[] actualText = new String[5];
        for (int i = 0; i < 5; i++) {
            actualText[i] = $$(".input__control").get(i).getValue();
        }
        assertAll(() -> assertEquals(cardNumber.substring(0, 16), actualText[0].replaceAll("\\s", ""), "Результат 1 не совпадает"),
                () -> assertEquals(month.substring(0, 2), actualText[1], "Результат 2 не совпадает"),
                () -> assertEquals(year.substring(0, 2), actualText[2], "Результат 3 не совпадает"),
                () -> assertEquals(name.substring(0, 27), actualText[3], "Результат 4 не совпадает"),
                () -> assertEquals(cvc.substring(0, 3), actualText[4], "Результат 5 не совпадает")
        );
    }

    @Test
    @DisplayName("Если ввести в поля количество символов, меньше валидного, появляется подсказка \"Неверный формат\" после нажатия \"Продолжить\"")
    void shouldBeInvalidFormatIfShort() {
        paymentForm = mainPage.openPaymentForm();
        String cardNumber = getInvalidCardNumberFormat(true);
        String month = getInvalidMonthFormat(true);
        String year = getInvalidYearFormat(true);
        String name = getInvalidNameFormat(true);
        String cvc = getInvalidCvc(true);
        paymentForm.fillCardData(cardNumber, month, year, name, cvc);
        paymentForm.expectedInvalidFormat();
    }

    @Test
    @DisplayName("При заполнении поля Владелец кириллицей поле не заполняется")
    void shouldNotFillNameField() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(true), getYear(true), getName(false), getValidCvc());
        String actualText = $$(".input__control").get(3).getValue();
        assertEquals("", actualText);
    }

    @Test
    @DisplayName("При заполнении поля Месяц и Год датой, меньше текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\"")
    void shouldNotAcceptExpiredData() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(false), getYear(false), getName(true), getValidCvc());
        paymentForm.expectedCardHasExpired();
    }
}
