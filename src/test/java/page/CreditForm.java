package page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreditForm {
    private final SelenideElement header = $(byText("Кредит по данным карты"));
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("[class='input__control']");
    private final SelenideElement cvcField = $("input[placeholder='999']");
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement successNotification = $(byText("Операция одобрена Банком."));
    private final SelenideElement failureNotification = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final ElementsCollection fieldsRedValidation = $$(".input__sub");
    private final SelenideElement expiredData = $(byText("Истёк срок действия карты"));

    public CreditForm() {
        header.shouldBe(visible);
    }

    public void fillCardData(String cardNumber, String month, String year, String name, String cvc) {
        cardNumberField.setValue(cardNumber);
        monthField.setValue(month);
        yearField.setValue(year);
        ownerField.setValue(name);
        cvcField.setValue(cvc);
        continueButton.click();
    }

    public void expectedSuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void expectedFiveFieldsMustBeFilled() {
        String expectedTexts = "Поле обязательно для заполнения";
        fieldsRedValidation.shouldHave(CollectionCondition.exactTexts(expectedTexts, expectedTexts, expectedTexts, expectedTexts, expectedTexts));
    }

    public void expectedFieldMustBeFilled() {
        String expectedText = "Поле обязательно для заполнения";
        fieldsRedValidation.shouldHave(CollectionCondition.exactTexts(expectedText));
    }

    public void expectedFailureNotification() {
        failureNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void expectedInvalidCardNumber() {
        assertAll(() -> $(byText("Недействительный номер карты")).shouldBe(visible),
                this::expectedFailureNotification
        );
    }

    public void expectedCardHasExpired() {
        expiredData.shouldBe(visible);
    }

    public void expectedBlankNameField() {
        ownerField.shouldBe(empty);
    }
}
