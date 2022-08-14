package com.github.wprusik.firecalculator;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Scanner;

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
        new FireCalculator().calculate();
    }

    public void calculate() throws IOException {
        System.out.printf("\n\nWelcome to\n%s%s\n%s%s%s\n", RED.code(), FIRE_LOGO.code(), BLUE.code(), FIRE_SUBLOGO.code(), RESET.code());
        SettingsReader.readSettingsFromFile();
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

    private void readInput() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("\nMonthly savings (%s): ", CURRENCY);
        MONTHLY_INCOME = readNumber(MONTHLY_INCOME, sc);
        System.out.print("Annual interest rate (%): ");
        YEARLY_INTEREST_RATE = readNumber(YEARLY_INTEREST_RATE, sc);
        System.out.print("Capitalization period (months): ");
        CAPITALIZATION_PERIOD_MONTHS = readNumber(CAPITALIZATION_PERIOD_MONTHS, sc).intValue();
        System.out.print("Average annual rate of return on accumulated capital (%): ");
        AVERAGE_ANNUAL_CAPITAL_RETURN_RATE = readNumber(AVERAGE_ANNUAL_CAPITAL_RETURN_RATE, sc);
        System.out.printf("Planned monthly withdraw (%s): ", CURRENCY);
        MONTHLY_WITHDRAW = readNumber(MONTHLY_WITHDRAW, sc);
        System.out.print("Whole withdrawal period (years): ");
        WITHDRAW_PERIOD_YEARS = readNumber(WITHDRAW_PERIOD_YEARS, sc).intValue();
        System.out.println("Thank you! Let's see what it looks like\n\n");
    }

    private Double readNumber(Number defaultValue, Scanner sc) {
        String text = sc.nextLine();
        try {
            return text.isBlank() ? defaultValue.doubleValue() : Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            System.out.println("Try again: ");
            return readNumber(defaultValue, sc);
        }
    }

    private double getInvestmentTarget() {
        return getSequenceValue(0D, WITHDRAW_PERIOD_YEARS);
    }

    private double getSequenceValue(double currentValue, int searchElementNumber) {
        if (searchElementNumber == 0) {
            return 0D;
        }
        double interestRate = getInterestRate();
        double outgo = (MONTHLY_WITHDRAW * 12);
        currentValue = getSequenceValue(currentValue, searchElementNumber - 1) / interestRate + outgo;
        return currentValue;
    }

    private double getInterestRate() {
        return (100D + AVERAGE_ANNUAL_CAPITAL_RETURN_RATE) / 100D;
    }
}
