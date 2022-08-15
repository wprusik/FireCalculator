package com.github.wprusik.firecalculator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.wprusik.firecalculator.Ansi.*;
import static com.github.wprusik.firecalculator.Settings.*;

public class TableBuilder {

    private static final int COLUMN_WIDTH = 30;
    private static final String COLUMN_SEPARATOR = "  ";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM yyyy");

    private static final String[] HEADERS = {"Date", "Income", "Savings"};

    static void generateTable(double cel) {
        System.out.println("\n");
        double total = 0;
        int month = 0;
        LocalDate currentDate = LocalDate.from(SAVING_START_DATE);
        printHeaders(currentDate.getYear());
        while (total < cel) {
            total += MONTHLY_INCOME;
            printRow(DATE_FORMAT.format(currentDate), MONTHLY_INCOME, total, null);

            month++;
            currentDate = currentDate.plusMonths(1L);

            if (month % CAPITALIZATION_PERIOD_MONTHS == 0) {
                double interest = total * (YEARLY_INTEREST_RATE * (CAPITALIZATION_PERIOD_MONTHS / 12F) / 100F);
                total += interest;
                printRow("Interest capitalization", interest, total, PURPLE);
                if (month % 12 == 0 && total < cel) {
                    printHeaders(currentDate.getYear());
                }
            }
        }

        System.out.printf("\n\nI need to save for %s%s%s months, which is %s%s%s years,",
                BLUE.code(), month, RESET.code(), BLUE.code(), month / 12, RESET.code());
        System.out.printf("\nto save required %s%s%s%s%s\n\n", UNDERLINE.code(), BOLD.code(), GREEN.code(), CURRENCY_FORMAT.format(total), RESET.code());
        System.out.println();
    }

    private static void printHeaders(int year) {
        StringBuilder str = new StringBuilder("\n\n");
        str.append(String.format("%s%sYEAR %s ----------------------------------------------------------------------%s%s\n",
                BLUE.code(), BOLD.code(), year, RESET.code(), BLUE.code()));
        for (String header : HEADERS) {
            str.append(withSpace(header)).append(COLUMN_SEPARATOR);
        }
        System.out.println(str.append(RESET.code()));
    }

    private static void printRow(String prefix, double income, double total, Ansi colour) {
        String str = (colour != null ? colour.code() : "") +
                withSpace(prefix) + COLUMN_SEPARATOR +
                withSpace(income) + COLUMN_SEPARATOR +
                withSpace(total) + COLUMN_SEPARATOR +
                (colour != null ? RESET.code() : "");
        System.out.println(str);
    }

    private static String withSpace(String str) {
        return str + getSpace(str.length());
    }

    private static String withSpace(Double value) {
        String str = CURRENCY_FORMAT.format(value);
        return str + getSpace(str.length());
    }

    private static String getSpace(int wordLength) {
        return " ".repeat(Math.max(0, COLUMN_WIDTH - wordLength));
    }

}
