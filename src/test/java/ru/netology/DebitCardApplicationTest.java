package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class DebitCardApplicationTest {
    SelenideElement form = $x("//form[contains(@class, form)]");
    SelenideElement notification = $x("//div[@data-test-id=\"notification\"]");

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String planningDate = generateDate(4);

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSubmitRequest() {
        Configuration.holdBrowserOpen = true;

        form.$x(".//span[@data-test-id=\"city\"]//input").setValue("Барнаул");
        form.$x(".//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$x(".//span[@data-test-id='date']//input").setValue(planningDate);
        form.$x(".//span[@data-test-id=\"name\"]//input").setValue("Юлия");
        form.$x(".//span[@data-test-id=\"phone\"]//input").setValue("+79600000000");
        form.$x(".//label[@data-test-id=\"agreement\"]").click();
        form.$x(".//span[contains(text(), 'Забронировать')]//ancestor::button").click();
        notification.should(visible, Duration.ofSeconds(15));
        notification.$x(".//div[@class='notification__title']").shouldHave(exactText("Успешно!"));
        notification.$x(".//div[@class='notification__content']").shouldHave(exactText("Встреча успешно забронирована на "
                + planningDate));
    }
}
