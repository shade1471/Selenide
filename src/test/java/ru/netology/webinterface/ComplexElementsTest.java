package ru.netology.webinterface;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class ComplexElementsTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldOrderCardUsingComplexElementsFirstPossibleDay() {
        //Заказ карты на ближайший доступный день

        String dateDelivery = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Но");
        $$(".menu > .menu-item").findBy(text("Новосибирск")).click();
        $("[data-test-id=date] .input__control").click();
        $$("tbody .calendar__row > [data-day]").first().click();
        $("[data-test-id=date] .input__control")
                .shouldHave(value(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + dateDelivery), Duration.ofSeconds(15));
    }

    @Test
    void OrderCardIfPossibleDateNextMonth() {
        //Заказ карты на дату через 2 недели от текущей

        String dateDelivery = LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        $("[data-test-id=city] .input__control").setValue("Но");
        $$(".menu > .menu-item").findBy(text("Новосибирск")).click();
        $("[data-test-id=date] .input__control").click();
        if ($$("tbody .calendar__row > .calendar__day_type_off[role=gridcell]")
                .findBy(text(LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("dd")))).isDisplayed()) {
            $(".calendar__arrow_direction_right.calendar__arrow_double + .calendar__arrow_direction_right").click();
            $$("tbody .calendar__row > [role=gridcell]")
                    .findBy(text(LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("dd")))).click();
        } else {
            $$("tbody .calendar__row > [role=gridcell]")
                    .findBy(text(LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("dd")))).click();
        }
        $("[data-test-id=name] .input__control").setValue("Андрей Грибанов");
        $("[data-test-id=phone] .input__control").setValue("+79200000000");
        $("[data-test-id=agreement]").click();
        $(byText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + dateDelivery), Duration.ofSeconds(15));
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
    void shouldCheckThatMonthAndYearAreCurrentForFirstPossibleDay() {
        $("[data-test-id=date] .input__control").click();
        if (LocalDate.now().plusDays(3).getMonth() == LocalDate.now().plusDays(28).getMonth()) {
            $(".calendar__name").shouldHave(text(LocalDate.now().plusDays(28).getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        } else {
            $(".calendar__name").shouldHave(text(LocalDate.now().getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        }
        $(".calendar__name").shouldHave(text(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))));
    }

    @Test
    void shouldIncreaseMonthByOneInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__arrow_direction_right.calendar__arrow_double + .calendar__arrow_direction_right").click();
        if (LocalDate.now().plusDays(3).getMonth() == LocalDate.now().plusDays(28).getMonth()) {
            $(".calendar__name").shouldHave(text(LocalDate.now().plusDays(56).getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        } else {
            $(".calendar__name").shouldHave(text(LocalDate.now().plusDays(28).getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        }
    }

    @Test
    void shouldIncreaseYearByOneInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__arrow_direction_right.calendar__arrow_double").click();
        $(".calendar__name").
                shouldHave(text(LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy"))));
    }

    @Test
    void shouldCheckImpossibilityDecreaseMonthForCurrentDate() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__arrow_direction_left.calendar__arrow_double + .calendar__arrow_direction_left[data-disabled=true]")
                .isEnabled();
    }

    @Test
    void shouldCheckImpossibilityDecreaseYearForCurrentDate() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__title > .calendar__arrow_direction_left.calendar__arrow_double[data-disabled=true]")
                .isEnabled();
    }

    @Test
    void shouldDecreaseByOneMonthInCalendar() {
        $("[data-test-id=date] .input__control").click();
        $(".calendar__arrow_direction_right.calendar__arrow_double + .calendar__arrow_direction_right").click();
        $(".calendar__arrow_direction_left.calendar__arrow_double + .calendar__arrow_direction_left").click();
        if (LocalDate.now().plusDays(3).getMonth() == LocalDate.now().plusMonths(1).getMonth()) {
            $(".calendar__name").shouldHave(text(LocalDate.now().plusMonths(1).getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        } else {
            $(".calendar__name").shouldHave(text(LocalDate.now().getMonth()
                    .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))));
        }
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
        $("[data-test-id=date] .input__control")
                .shouldHave(value(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }

}