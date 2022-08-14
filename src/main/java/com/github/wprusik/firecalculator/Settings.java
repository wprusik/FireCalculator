package com.github.wprusik.firecalculator;

import java.text.NumberFormat;
import java.time.LocalDate;

public class Settings {

    static double MONTHLY_INCOME;                       // miesięczny przychód
    static double YEARLY_INTEREST_RATE;                 // roczna stopa oprocentowania lokaty
    static int CAPITALIZATION_PERIOD_MONTHS;            // okres kapitalizacji
    static double AVERAGE_ANNUAL_CAPITAL_RETURN_RATE;   // średnia roczna stopa zwrotu z kapitału
    static double MONTHLY_WITHDRAW;                     // miesięcznie będę wypłacał
    static int WITHDRAW_PERIOD_YEARS;                   // przez x lat

    static LocalDate SAVING_START_DATE = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    static String CURRENCY = CURRENCY_FORMAT.getCurrency().getDisplayName();
    static boolean ANSI_ENABLED;
}
