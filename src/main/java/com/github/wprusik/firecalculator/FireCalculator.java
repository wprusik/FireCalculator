package com.github.wprusik.firecalculator;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;

import static com.github.wprusik.firecalculator.Ansi.*;

public class FireCalculator {

    // default settings
    static double MONTHLY_INCOME;                       // miesięczny przychód
    static double YEARLY_INTEREST_RATE;                 // roczna stopa oprocentowania lokaty
    static int CAPITALIZATION_PERIOD_MONTHS;            // okres kapitalizacji
    static double AVERAGE_ANNUAL_CAPITAL_RETURN_RATE;   // średnia roczna stopa zwrotu z kapitału
    static double MONTHLY_WITHDRAW;                     // miesięcznie będę wypłacał
    static int WITHDRAW_PERIOD_YEARS;                   // przez x lat

    static LocalDate SAVING_START_DATE = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    static String CURRENCY = CURRENCY_FORMAT.getCurrency().getDisplayName();

    public static void main(String... args) throws IOException {
        System.out.printf("\n\nWelcome to\n%s%s\n%s%s%s\n", RED.code(), FIRE_LOGO.code(), BLUE.code(), FIRE_SUBLOGO.code(), RESET.code());
        SettingsReader.readSettings();
        calculate();
    }

    public static void calculate() {
        System.out.printf("\nMonthly deposit: %s%s%s", YELLOW.code(), CURRENCY_FORMAT.format(MONTHLY_INCOME), RESET.code());
        System.out.printf("\nYearly interest rate: %s %%", YEARLY_INTEREST_RATE);
        System.out.printf("\nCapitalization period: %s mies.", CAPITALIZATION_PERIOD_MONTHS);
        System.out.printf("\n\nI plan to withdraw %s%s%s every month", GREEN.code(), CURRENCY_FORMAT.format(MONTHLY_WITHDRAW), RESET.code());
        System.out.printf("\nFor %d years", WITHDRAW_PERIOD_YEARS);
        System.out.printf("\nSummary withdraw: %s", CURRENCY_FORMAT.format(12 * WITHDRAW_PERIOD_YEARS * MONTHLY_WITHDRAW));
        System.out.printf("\nAverage annual rate of return on capital: %s %%", AVERAGE_ANNUAL_CAPITAL_RETURN_RATE);
        double target = getInvestmentTarget();
        System.out.printf("\n\nTarget: %s%s%s%s\n", BOLD.code(), RED.code(), CURRENCY_FORMAT.format(target), RESET.code());
        new TableBuilder().generateTable(target);
    }

    private static double getInvestmentTarget() {
        return getSequenceValue(0D, WITHDRAW_PERIOD_YEARS);
    }

    private static double getSequenceValue(double currentValue, int searchElementNumber) {
        if (searchElementNumber == 0) {
            return 0D;
        }
        double interestRate = getInterestRate();
        double outgo = (MONTHLY_WITHDRAW * 12);
        currentValue = getSequenceValue(currentValue, searchElementNumber - 1) / interestRate + outgo;
        return currentValue;
    }

    private static double getInterestRate() {
        return (100D + AVERAGE_ANNUAL_CAPITAL_RETURN_RATE) / 100D;
    }
}
