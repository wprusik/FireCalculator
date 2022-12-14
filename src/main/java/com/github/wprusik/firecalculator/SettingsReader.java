package com.github.wprusik.firecalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

import static java.lang.String.valueOf;

public class SettingsReader {

    static void readSettings() throws IOException {
        readResourceProperties();
        readUserProperties();
    }

    private static void readResourceProperties() throws IOException {
        Properties props = getResourceProperties();
        setVariables(props);
    }

    private static void readUserProperties() {
        File propertiesFile = getUserPropertiesFile();
        if (propertiesFile.exists()) {
            System.out.printf("%s\nReading properties from file... %s", Ansi.WHITE.code(), Ansi.RESET.code());
            if (readProperties(propertiesFile)) {
                System.out.printf("%sSettings successfully loaded.%s\n\n\n", Ansi.WHITE.code(), Ansi.RESET.code());
            }
        } else {
            System.out.printf("\n%sProperties file not found.\n\n\n%s", Ansi.WHITE.code(), Ansi.RESET.code());
        }
    }

    private static boolean readProperties(File propertiesFile) {
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(propertiesFile)) {
            props.load(is);
        } catch (IOException e) {
            System.out.printf("\n%sFailed to load settings - input/output error%s", Ansi.RED.code(), Ansi.RESET.code());
            return false;
        }
        setVariables(props);
        return true;
    }

    private static void setVariables(Properties props) {
        Settings.MONTHLY_INCOME = getDoubleProperty(props, "savings.monthly-deposit", Settings.MONTHLY_INCOME);
        Settings.YEARLY_INTEREST_RATE = getDoubleProperty(props, "savings.annual-interest", Settings.YEARLY_INTEREST_RATE);
        Settings.CAPITALIZATION_PERIOD_MONTHS = getIntProperty(props, "savings.capitalization-period.months", Settings.CAPITALIZATION_PERIOD_MONTHS);
        Settings.SAVING_START_DATE = getDateProperty(props, "savings.start-date.month-year", Settings.SAVING_START_DATE);
        Settings.AVERAGE_ANNUAL_CAPITAL_RETURN_RATE = getDoubleProperty(props, "capital.average-annual-interest-rate", Settings.AVERAGE_ANNUAL_CAPITAL_RETURN_RATE);
        Settings.MONTHLY_WITHDRAW = getDoubleProperty(props, "capital.withdraw.monthly-amount", Settings.MONTHLY_WITHDRAW);
        Settings.WITHDRAW_PERIOD_YEARS = getIntProperty(props, "capital.withdraw.period.years", Settings.WITHDRAW_PERIOD_YEARS);
        Settings.CURRENCY = props.getProperty("currency", Settings.CURRENCY);
        Optional.ofNullable(props.getProperty("ansi-support.enabled")).map(Boolean::valueOf).ifPresent(enabled -> Settings.ANSI_ENABLED = enabled);
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
            throw new IllegalArgumentException("Invalid date number in property " + key);
        }
        return LocalDate.of(Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), 1);
    }

    private static File getUserPropertiesFile() {
        String propsFilePath = new File(SettingsReader.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile().getAbsolutePath() + "/application.properties";
        return new File(propsFilePath);
    }

    private static Properties getResourceProperties() throws IOException {
        try (InputStream is = SettingsReader.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(is);
            return props;
        }
    }
}
