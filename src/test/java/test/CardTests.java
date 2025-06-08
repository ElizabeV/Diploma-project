package test;


import com.codeborne.selenide.logevents.SelenideLogger;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import page.MainPage;
import page.PaymentForm;

import static com.codeborne.selenide.Selenide.*;
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
    @DisplayName("1. При заполнении формы валидными данными всех полей и нажатии кнопки \"Продолжить\" появляется уведомление об успешной оплате в форме \"Оплата по карте\".")
    void happyPathWithPaymentCard() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(0), getYear(0), getName("en"), getValidCvc());
        paymentForm.expectedSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getPayStatus());
    }
    @Test
    @DisplayName("2. При нажатии кнопки \"Продолжить\" формы с пустыми полями, у каждого поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfAllFieldsIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(null, null, null, null, null);
        paymentForm.expectedFiveFieldsMustBeFilled(
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения",
                "Поле обязательно для заполнения"
        );
    }
    @Test
    @DisplayName("3. При нажатии кнопки \"Продолжить\" формы с пустым полем Номер карты у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfCardFieldIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(null, getMonth(0), getYear(1), getName("en"), getValidCvc());
        paymentForm.expectedFieldMustBeFilled("Поле обязательно для заполнения");
    }
    @Test
    @DisplayName("4. При нажатии кнопки \"Продолжить\" формы с пустым полем Месяц у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfMonthFieldIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), null, getYear(1), getName("en"), getValidCvc());
        paymentForm.expectedFieldMustBeFilled("Поле обязательно для заполнения");
    }
    @Test
    @DisplayName("5. При нажатии кнопки \"Продолжить\" формы с пустым полем Год у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfYearFieldIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(5), null, getName("en"), getValidCvc());
        paymentForm.expectedFieldMustBeFilled("Поле обязательно для заполнения");
    }
    @Test
    @DisplayName("6. При нажатии кнопки \"Продолжить\" формы с пустым полем Владелец у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfOwnerFieldIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(5), getYear(0), null, getValidCvc());
        paymentForm.expectedFieldMustBeFilled("Поле обязательно для заполнения");
    }
    @Test
    @DisplayName("7. При нажатии кнопки \"Продолжить\" формы с пустым полем CVC/CVV у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Оплата по карте\"")
    void shouldFailPayIfCvcFieldIsEmpty() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(5), getYear(0), getName("en"), null);
        paymentForm.expectedFieldMustBeFilled("Поле обязательно для заполнения");
    }
    @Test
    @DisplayName("8. При заполнении поля номера карты данными заблокированной карты происходит отказ в проведении операции в форме \"Оплата по карте\"")
    void shouldPaymentDenyWithDeclinedCard() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getDeclinedCardNumber(), getMonth(1), getYear(1), getName("en"), getValidCvc());
        assertAll(() -> paymentForm.expectedFailureNotification(),
                () -> assertEquals("DECLINED", SQLHelper.getPayStatus())
        );
    }
    @Test
    @DisplayName("9. При заполнении поля номера карты 16 случайными цифрами происходит отказ в проведении операции в форме \"Оплата по карте\"")
    void shouldBeRejectIfRandom16Symbols() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getInvalidCardNumberFormat(false), getMonth(1), getYear(3), getName("en"), getValidCvc());
        paymentForm.expectedFailureNotification();
    }
    @Test
    @DisplayName("10. При заполнении поля CVC/CVV количеством символов, меньше валидного, появляется подсказка \"Неверный формат\" в форме \"Оплата по карте\"")
    void shouldBeInvalidFormatIfShortCVC() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(1), getYear(1), getName("en"), getShortCvc());
        paymentForm.expectedInvalidFormat();
    }
    @Test
    @DisplayName("11. При заполнении поля Год датой, меньше текущей, а поля Месяц текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\" в форме \"Оплата по карте\"")
    void shouldNotAcceptExpiredYear() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(0), getYear(-2), getName("en"), getValidCvc());
        paymentForm.expectedCardHasExpired();
    }
    @Test
    @DisplayName("12. При заполнении поля Месяц датой, меньше текущей, а поля Год текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\" в форме \"Оплата по карте\"")
    void shouldNotAcceptExpiredMonth() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(-1), getYear(0), getName("en"), getValidCvc());
        paymentForm.expectedCardHasExpired();
    }
    @Test
    @DisplayName("13. При заполнении поля Владелец кириллицей поле не заполняется в форме \"Оплата по карте\"")
    void shouldNotFillNameField() {
        paymentForm = mainPage.openPaymentForm();
        paymentForm.fillCardData(getApprovedCardNumber(), getMonth(1), getYear(4), getName("ru"), getValidCvc());
        paymentForm.expectedBlankNameField();
    }
}