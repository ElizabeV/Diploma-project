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
    @DisplayName("По кнокпе \"Купить в кредит\" открывается форма \"Кредит по данным карты\"")
    void shouldOpenCreditFormWithButtonBuyByCredit() {
        mainPage.openCreditForm();
    }

    @Test
    @DisplayName("При заполнении формы \"Кредит по данным карты\" валидными данными всех полей и нажатии кнопки \"Продолжить\" появляется уведомление об успешной оплате")
    void happyPathWithCreditCard() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getApprovedCardNumber(), getMonth(true), getYear(true), getName(true), getValidCvc());
        creditForm.expectedSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("При нажатии кнопки \"Продолжить\" формы \"Кредит по данным карты\" с пустыми полями, у каждого поля появляется подсказка \"Поле обязательно для заполнения\"")
    void shouldFailPayIfAllFieldsIsEmpty() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(null, null, null, null, null);
        creditForm.expectedFieldMustBeFilled();
    }

    @Test
    @DisplayName("При заполнении поля номера карты данными заблокированной карты происходит отказ в оплате в форме \"Кредит по данным карты\"")
    void shouldPaymentDenyWithDeclinedCard() {
        creditForm = mainPage.openCreditForm();
        creditForm.fillCardData(getDeclinedCardNumber(), getMonth(true), getYear(true), getName(true), getValidCvc());
        assertAll(() -> creditForm.expectedFailureNotification(),
                () -> assertEquals("DECLINED", SQLHelper.getCreditStatus())
        );
    }
}
