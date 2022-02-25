package ru.netology.webinterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ComplexElementsTests {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        $("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
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

    @Test
    void shouldCheckThatMonthAndYearAreCurrentInCalendarIfFieldIsEmpty() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__name").shouldHave(text(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        $(".calendar__name").shouldHave(text(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))));
    }

    @Test
    void shouldIncreaseMonthByOneInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_right.calendar__arrow_double + .calendar__arrow_direction_right").click();
        $(".calendar__name").shouldHave(text(LocalDate.now().plusMonths(1).getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
    }

    @Test
    void shouldIncreaseYearByOneInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_right.calendar__arrow_double").click();
        $(".calendar__name").shouldHave(text(LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy")))); //.getDisplayName(TextStyle.FULL_STANDALONE,Locale.forLanguageTag("ru"))));
    }

    @Test
    void shouldCheckImpossibilityDecreaseMonthForCurrentDate() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_left.calendar__arrow_double + .calendar__arrow_direction_left[data-disabled=true]").isEnabled();
    }

    @Test
    void shouldCheckImpossibilityDecreaseYearForCurrentDate() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_left.calendar__arrow_double[data-disabled=true]").isEnabled();
    }

    @Test
    void shouldDecreaseByOneMonthInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_right.calendar__arrow_double + .calendar__arrow_direction_right").click();
        $(".calendar__title > .calendar__arrow_direction_left.calendar__arrow_double + .calendar__arrow_direction_left").click();
        $(".calendar__name").shouldHave(text(LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
    }

    @Test
    void shouldDecreaseByOneYearInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_right.calendar__arrow_double").click();
        $(".calendar__title > .calendar__arrow_direction_left.calendar__arrow_double").click();
        $(".calendar__name").shouldHave(text(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))));
    }

    @Test
    void shouldSelectDateFromCalendarByFirstPossible() {
        $("[data-test-id=date] .input__control").click();
        $$("tbody .calendar__row > [data-day]").first().click();
        $("[data-test-id=date] .input__control").shouldHave(value(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

}
