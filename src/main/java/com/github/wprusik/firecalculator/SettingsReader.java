package com.github.wprusik.firecalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static java.lang.String.valueOf;

public class SettingsReader {

    static void readSettingsFromFile() {
        File propertiesFile = getPropertiesFile();
        if (propertiesFile.exists()) {
            System.out.printf("\n%sOdczytuję właściwości z pliku... %s", Ansi.WHITE.code(), Ansi.RESET.code());
            if (readProperties(propertiesFile)) {
                System.out.printf("%sPoprawnie załadowano ustawienia.%s\n\n\n", Ansi.WHITE.code(), Ansi.RESET.code());
            }
        } else {
            System.out.printf("\n%sNie znaleziono pliku z właściwościami.\n\n\n%s", Ansi.WHITE.code(), Ansi.RESET.code());
        }
    }

    private static boolean readProperties(File propertiesFile) {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile)) {
            props.load(is);
        } catch (IOException e) {
            System.out.printf("\n%sNie udało się załadować ustawień - błąd odczytu%s", Ansi.RED.code(), Ansi.RESET.code());
            return false;
        }
        setVariables(props);
        return true;
    }

    private static void setVariables(Properties props) {
        FireCalculator.MONTHLY_INCOME = getDoubleProperty(props, "lokata.wplacam-miesiecznie", FireCalculator.MONTHLY_INCOME);
        FireCalculator.YEARLY_INTEREST_RATE = getDoubleProperty(props, "lokata.oprocentowanie-roczne", FireCalculator.YEARLY_INTEREST_RATE);
            FireCalculator.CAPITALIZATION_PERIOD_MONTHS = getIntProperty(props, "lokata.okres-kapitalizacji.mies", FireCalculator.CAPITALIZATION_PERIOD_MONTHS);
        FireCalculator.SAVING_START_DATE = getDateProperty(props, "lokata.miesiac-rozpoczecia", FireCalculator.SAVING_START_DATE);
        FireCalculator.AVERAGE_ANNUAL_CAPITAL_RETURN_RATE = getDoubleProperty(props, "kapital.srednia-roczna-stopa-oprocentowania", FireCalculator.AVERAGE_ANNUAL_CAPITAL_RETURN_RATE);
        FireCalculator.MONTHLY_WITHDRAW = getDoubleProperty(props, "kapital.wyplata-miesieczna", FireCalculator.MONTHLY_WITHDRAW);
        FireCalculator.WITHDRAW_PERIOD_YEARS = getIntProperty(props, "kapital.okres-wyplaty.lat", FireCalculator.WITHDRAW_PERIOD_YEARS);
        FireCalculator.CURRENCY = props.getProperty("waluta", FireCalculator.CURRENCY);
    }

    private static Double getDoubleProperty(Properties props, String key, double defaultValue) {
        String val = props.getProperty(key, valueOf(defaultValue));
        return Double.parseDouble(val);
    }

    private static Integer getIntProperty(Properties props, String key, int defaultValue) {
        String val = props.getProperty(key, valueOf(defaultValue));
        return Integer.parseInt(val);
    }

    @SuppressWarnings("SameParameterValue")
    private static LocalDate getDateProperty(Properties props, String key, LocalDate defaultValue) {
        String val = props.getProperty(key, DateTimeFormatter.ofPattern("MM.yyyy").format(defaultValue));
        String[] parts = val.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Niewłaściwy numer daty we właściwości " + key);
        }
        return LocalDate.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), 1);
    }

    private static File getPropertiesFile() {
        String propsFilePath = new File(SettingsReader.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath() + "/application.properties";
        return new File(propsFilePath);
    }
}
