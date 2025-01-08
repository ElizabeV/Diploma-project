package page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement header = $(byText("Путешествие дня"));
    private final SelenideElement buyButton = $("button").find(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));

    public MainPage() {
        header.shouldBe(visible);
        buyButton.shouldBe(visible);
        creditButton.shouldBe(visible);
    }

    public PaymentForm openPaymentForm() {
        buyButton.click();
        return new PaymentForm();
    }

    public CreditForm openCreditForm() {
        creditButton.click();
        return new CreditForm();
    }

}
