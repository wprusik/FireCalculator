package com.github.wprusik.firecalculator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.github.wprusik.firecalculator.Ansi.*;
import static com.github.wprusik.firecalculator.FireCalculator.*;

public class TableBuilder {

    private static final int COLUMN_WIDTH = 30;
    private static final String COLUMN_SEPARATOR = "  ";
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM yyyy");

    private static final String[] HEADERS = {"Data", "Przychód", "Oszczędności"};

    void generateTable(double cel) {
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
                printRow("Kapitalizacja odsetek", interest, total, PURPLE);
                if (month % 12 == 0 && total < cel) {
                    printHeaders(currentDate.getYear());
                }
            }
        }

        System.out.printf("\n\nMuszę oszczędzać przez %s%s%s miesięcy, czyli %s%s%s lat,",
                BLUE.code(), month, RESET.code(), BLUE.code(), month / 12, RESET.code());
        System.out.printf("\nby odłożyć potrzebne %s%s%s%s%s\n\n", UNDERLINE.code(), BOLD.code(), GREEN.code(), CURRENCY_FORMAT.format(total), RESET.code());
        System.out.println();
    }

    private void printHeaders(int year) {
        StringBuilder str = new StringBuilder("\n\n");
        str.append(String.format("%s%sROK %s ----------------------------------------------------------------------%s%s\n",
                BLUE.code(), BOLD.code(), year, RESET.code(), BLUE.code()));
        for (String header : HEADERS) {
            str.append(withSpace(header)).append(COLUMN_SEPARATOR);
        }
        System.out.println(str.append(RESET.code()));
    }

    private void printRow(String prefix, double income, double total, Ansi colour) {
        String str = (colour != null ? colour.code() : "") +
                withSpace(prefix) + COLUMN_SEPARATOR +
                withSpace(income) + COLUMN_SEPARATOR +
                withSpace(total) + COLUMN_SEPARATOR +
                (colour != null ? RESET.code() : "");
        System.out.println(str);
    }

    private String withSpace(String str) {
        return str + getSpace(str.length());
    }

    private String withSpace(Double value) {
        int formatterExtension = (",00 " + CURRENCY).length() + Double.valueOf(Math.pow(value, 1 / 1000F)).intValue();
        int length = Double.valueOf(Math.log10(value)).intValue() + 1 + formatterExtension;
        return CURRENCY_FORMAT.format(value) + getSpace(length);
    }

    private String getSpace(int wordLength) {
        return " ".repeat(Math.max(0, COLUMN_WIDTH - wordLength));
    }

}
