package page;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public CreditForm(){
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

    public void expectedFieldMustBeFilled() {
        String expectedTexts = "Поле обязательно для заполнения";
        String[] actualText = new String[5];
        for (int i = 0; i < 5; i++) {
            actualText[i] = $$(".input__sub").get(i).getText();
        }
        assertAll( () -> assertEquals(expectedTexts, actualText[0], "Результат 1 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[1], "Результат 2 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[2], "Результат 3 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[3], "Результат 4 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[4], "Результат 5 не совпадает")
        );
    }

    public void expectedFailureNotification() {
        failureNotification.shouldBe(visible, Duration.ofSeconds(15));
    }
}
