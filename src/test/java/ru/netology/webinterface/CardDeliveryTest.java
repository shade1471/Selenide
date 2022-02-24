package ru.netology.webinterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        $("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
    }


    @Test
    void shouldOrderCard() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на "), Duration.ofSeconds(15));
    }

    @Test
    void shouldOrderCardWithEmptyFieldByCity() {
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldOrderCardWithEmptyFieldByName() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldOrderCardWithEmptyFieldByPhone() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    void shouldOrderCardWithoutCheckBox() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $(byText("Забронировать")).click();
        $(".checkbox.input_invalid").shouldBe(visible);
    }

    @Test
    void shouldOrderCardWithDateLessThreeDays() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldSendFormWithDoubleSurname() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Михаил Салтыков-Щедрин");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на "), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormByNameOfCityWithDash() {
        $("[data-test-id=city] .input__control").setValue("Йошкар-Ола");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на "), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendFormWithNotCyrillicSymbolsByName() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Andrey Gribanov");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(text("Имя и Фамилия указаные неверно."));
    }

    @Test
    public void shouldSendFormWithCityNotInList() {
        $("[data-test-id=city] .input__control").setValue("Северск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldOrderCardWithPhone12Digits() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+712345678901");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldOrderCardWithPhone10Digits() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+7123456789");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldOrderCardWithPhoneWith11DigitsAndWithOutPlusSeven() {
        $("[data-test-id=city] .input__control").setValue("Новосибирск");
        $("[data-test-id=date] .input__control").setValue(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("81234567890");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(text("Телефон указан неверно"));
    }

    @Test
    void shouldCheckListOfCities() {
        $("[data-test-id=city] .input__control").setValue("Нов");
        $$(".menu > .menu-item").filter(visible).get(0).shouldHave(text("Великий Новгород"));
        $$(".menu > .menu-item").filter(visible).get(1).shouldHave(text("Иваново"));
        $$(".menu > .menu-item").filter(visible).get(2).shouldHave(text("Нижний Новгород"));
        $$(".menu > .menu-item").filter(visible).get(3).shouldHave(text("Новосибирск"));
        $$(".menu > .menu-item").filter(visible).get(4).shouldHave(text("Ульяновск"));
    }

    @Test
    void shouldCheckAutoCompleteByFieldCityWithOneChar() {
        $("[data-test-id=city] .input__control").setValue("Н");
        $$(".menu > .menu-item").isEmpty();

    }

    @Test
    void shouldSetCityUsingForm() {
        $("[data-test-id=city] .input__control").setValue("Нов");
        $$(".menu > .menu-item").findBy(text("Новосибирск")).click();
        $("[data-test-id=city] .input__control").shouldHave(value("Новосибирск"));
    }

    @Test
    void shouldCheckSizeListOptions() {
        $("[data-test-id=city] .input__control").setValue("Мо");
        $$(".menu > .menu-item").shouldHave(sizeGreaterThan(1));
    }

}
