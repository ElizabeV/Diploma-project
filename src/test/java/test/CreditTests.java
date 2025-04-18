package test;

import data.SQLHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.CreditForm;
import page.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTests {
    MainPage mainPage;
    CreditForm creditForm;

    @BeforeEach
    void setup() {
        mainPage = open("http://localhost:8080", MainPage.class);
    }

    @Test
    @DisplayName("14. При заполнении формы валидными данными всех полей и нажатии кнопки \"Продолжить\" появляется уведомление об успешной оплате в форме \"Кредит по данным карты\"")
    void happyPathWithCreditCard() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(1), getYear(0), getName("en"), getValidCvc());
        creditForm.expectedSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }
    @Test
    @DisplayName("15. При нажатии кнопки \"Продолжить\" формы с пустыми полями, у каждого поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfAllFieldsIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(null, null, null, null, null);
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("16. При нажатии кнопки \"Продолжить\" формы с пустым полем Номер карты у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfCardFieldIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(null, getMonth(0), getYear(1), getName("en"), getValidCvc());
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("17. При нажатии кнопки \"Продолжить\" формы с пустым полем Месяц у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfMonthFieldIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), null, getYear(1), getName("en"), getValidCvc());
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("18. При нажатии кнопки \"Продолжить\" формы с пустым полем Год у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfYearFieldIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(5), null, getName("en"), getValidCvc());
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("19. При нажатии кнопки \"Продолжить\" формы с пустым полем Владелец у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfOwnerFieldIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(5), getYear(0), null, getValidCvc());
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("20. При нажатии кнопки \"Продолжить\" формы с пустым полем CVC/CVV у поля появляется подсказка \"Поле обязательно для заполнения\" в форме \"Кредит по данным карты\"")
    void shouldFailPayIfCvcFieldIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(5), getYear(0), getName("en"), null);
        creditForm.expectedFieldMustBeFilled();
    }
    @Test
    @DisplayName("21. При заполнении поля номера карты данными заблокированной карты происходит отказ в проведении операции в форме \"Кредит по данным карты\"")
    void shouldPaymentDenyWithDeclinedCard() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getDeclinedCardNumber(), getMonth(1), getYear(1), getName("en"), getValidCvc());
        assertAll(() -> creditForm.expectedFailureNotification(),
                () -> assertEquals("DECLINED", SQLHelper.getCreditStatus())
        );
    }
    @Test
    @DisplayName("22. При заполнении поля номера карты 16 случайными символами происходит отказ в проведении операции в форме \"Кредит по данным карты\"")
    void shouldBeRejectIfRandom16Symbols() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getInvalidCardNumberFormat(false), getMonth(1), getYear(3), getName("en"), getValidCvc());
        creditForm.expectedInvalidCardNumber();
    }
    @Test
    @DisplayName("23. При заполнении поля Месяц и Год датой, меньше текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\" в форме \"Кредит по данным карты\"")
    void shouldNotAcceptExpiredDate() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(-3), getYear(-1), getName("en"), getValidCvc());
        creditForm.expectedCardHasExpired();
    }
    @Test
    @DisplayName("24. При заполнении поля Год датой, меньше текущей, а поля Месяц текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\" в форме \"Кредит по данным карты\"")
    void shouldNotAcceptExpiredYear() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(0), getYear(-2), getName("en"), getValidCvc());
        creditForm.expectedCardHasExpired();
    }
    @Test
    @DisplayName("25. При заполнении поля Месяц датой, меньше текущей, а поля Год текущей, после нажатия кнопки \"Продолжить\" появляется подсказка \"Истек срок действия карты\" в форме \"Кредит по данным карты\"")
    void shouldNotAcceptExpiredMonth() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(-1), getYear(0), getName("en"), getValidCvc());
        creditForm.expectedCardHasExpired();
    }
    @Test
    @DisplayName("26. При заполнении поля Владелец кириллицей поле не заполняется в форме \"Кредит по данным карты\"\n")
    void shouldNotFillNameField() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(1), getYear(4), getName("ru"), getValidCvc());
        creditForm.expectedBlankNameField();
    }
}
