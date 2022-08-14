package com.github.wprusik.firecalculator;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Scanner;

import static com.github.wprusik.firecalculator.Ansi.*;

public class FireCalculator {

    // default settings
    static double MONTHLY_INCOME = 5000;                    // miesięczny przychód
    static double YEARLY_INTEREST_RATE = 6.5;               // roczna stopa oprocentowania lokaty
    static int CAPITALIZATION_PERIOD_MONTHS = 6;            // okres kapitalizacji
    static double AVERAGE_ANNUAL_CAPITAL_RETURN_RATE = 4;   // średnia roczna stopa zwrotu z kapitału
    static double MONTHLY_WITHDRAW = 7000;                  // miesięcznie będę wypłacał
    static int WITHDRAW_PERIOD_YEARS = 40;                  // przez x lat

    static LocalDate SAVING_START_DATE = LocalDate.of(LocalDate.now().getYear() + 1, 1, 1);
    static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    static String CURRENCY = CURRENCY_FORMAT.getCurrency().getDisplayName();

    public static void main(String... args) {
        new FireCalculator().calculate();
    }

    public void calculate() {
        System.out.printf("\n\nWelcome to\n%s%s\n%s%s%s\n", RED.code(), FIRE_LOGO.code(), BLUE.code(), FIRE_SUBLOGO.code(), RESET.code());
        SettingsReader.readSettingsFromFile();
        System.out.printf("\nMiesięcznie odkładam: %s%s%s", YELLOW.code(), CURRENCY_FORMAT.format(MONTHLY_INCOME), RESET.code());
        System.out.printf("\nStopa oprocentowania: %s %%", YEARLY_INTEREST_RATE);
        System.out.printf("\nOkres kapitalizacji: %s mies.", CAPITALIZATION_PERIOD_MONTHS);
        System.out.printf("\n\nMiesięcznie chcę wybierać %s%s%s", GREEN.code(), CURRENCY_FORMAT.format(MONTHLY_WITHDRAW), RESET.code());
        System.out.printf("\nPrzez lat %d", WITHDRAW_PERIOD_YEARS);
        System.out.printf("\nW sumie wybiorę: %s", CURRENCY_FORMAT.format(12 * WITHDRAW_PERIOD_YEARS * MONTHLY_WITHDRAW));
        System.out.printf("\nŚrednia roczna stopa zwrotu z kapitału: %s %%", AVERAGE_ANNUAL_CAPITAL_RETURN_RATE);
        double target = getInvestmentTarget();
        System.out.printf("\n\nCel: %s%s%s%s\n", BOLD.code(), RED.code(), CURRENCY_FORMAT.format(target), RESET.code());
        new TableBuilder().generateTable(target);
    }

    private void readInput() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("\nMiesięcznie odkładam (%s): ", CURRENCY);
        MONTHLY_INCOME = readNumber(MONTHLY_INCOME, sc);
        System.out.print("Roczna stopa oprocentowania (%): ");
        YEARLY_INTEREST_RATE = readNumber(YEARLY_INTEREST_RATE, sc);
        System.out.print("Okres kapitalizacji (mies.): ");
        CAPITALIZATION_PERIOD_MONTHS = readNumber(CAPITALIZATION_PERIOD_MONTHS, sc).intValue();
        System.out.print("Średnia roczna stopa zwrotu ze zgromadzonego kapitału (%): ");
        AVERAGE_ANNUAL_CAPITAL_RETURN_RATE = readNumber(AVERAGE_ANNUAL_CAPITAL_RETURN_RATE, sc);
        System.out.printf("Miesięcznie chcę wybierać (%s): ", CURRENCY);
        MONTHLY_WITHDRAW = readNumber(MONTHLY_WITHDRAW, sc);
        System.out.print("Okres wypłaty środków (lat): ");
        WITHDRAW_PERIOD_YEARS = readNumber(WITHDRAW_PERIOD_YEARS, sc).intValue();
        System.out.println("Dzięki! Sprawdźmy jak to wygląda\n\n");
    }

    private Double readNumber(Number defaultValue, Scanner sc) {
        String text = sc.nextLine();
        try {
            return text.isBlank() ? defaultValue.doubleValue() : Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            System.out.println("Spróbuj jeszcze raz: ");
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
