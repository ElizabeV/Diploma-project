package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentForm {
    private final SelenideElement header = $(byText("Оплата по карте"));
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("[class='input__control']");
    private final SelenideElement cvcField = $("input[placeholder='999']");
    private final SelenideElement continueButton = $(byText("Продолжить"));
    private final SelenideElement successNotification = $(byText("Операция одобрена Банком."));
    private final SelenideElement failureNotification = $(byText("Ошибка! Банк отказал в проведении операции."));
    private final ElementsCollection invalidFormat = $$(byText("Неверный формат"));
    private final ElementsCollection fieldsRedValidation = $$(".input__sub");
    private final SelenideElement expiredData = $(byText("Истёк срок действия карты"));

    public PaymentForm() {
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
            actualText[i] = fieldsRedValidation.get(i).getText();
        }
        assertAll(() -> assertEquals(expectedTexts, actualText[0], "Результат 1 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[1], "Результат 2 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[2], "Результат 3 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[3], "Результат 4 не совпадает"),
                () -> assertEquals(expectedTexts, actualText[4], "Результат 5 не совпадает")
        );
    }

    public void expectedFailureNotification() {
        failureNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void expectedInvalidFormat() {
        String expectedTexts = "Неверный формат";
        String[] actualText = new String[5];
        for (int i = 0; i < 4; i++) {
            actualText[i] = invalidFormat.get(i).getText();
        }
        try {
            actualText[4] = invalidFormat.get(4).getText();
            assertAll(() -> assertEquals(expectedTexts, actualText[0], "Результат 1 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[1], "Результат 2 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[2], "Результат 3 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[3], "Результат 4 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[4], "Результат 5 не совпадает")
            );
        } catch (IndexOutOfBoundsException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Отсутствует валидация для некоторых полей");
            assertAll(() -> assertEquals(expectedTexts, actualText[0], "Результат 1 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[1], "Результат 2 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[2], "Результат 3 не совпадает"),
                    () -> assertEquals(expectedTexts, actualText[3], "Результат 4 не совпадает"),
                    () -> assertEquals(expectedTexts, "", "Результат 5 не совпадает")
            );
        }
    }

    public void expectedCardHasExpired() {
        expiredData.shouldBe(visible);
    }
}
