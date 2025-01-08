package data;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getInvalidCardNumberFormat(boolean isShort) {
        Random random = new Random();
        int length = isShort ? random.nextInt(14) + 1 : random.nextInt(8) + 17;
        String number = "";
        for (int i = 0; i < length; i++) {
            number = number + random.nextInt(10);
        }
        return number;
    }

    public static String getMonth(boolean isValid) {
        int currentMonth = LocalDate.now().getMonthValue();
        int month = isValid ? currentMonth : currentMonth - 1;
        if (LocalDate.now().getMonthValue() == 1 && !isValid) {
            month = 12;
        }
        return String.format("%02d", month);
    }

    public static String getInvalidMonthFormat(boolean isShort) {
        Random random = new Random();
        int length = isShort ? 1 : 3;
        String month = "";
        for (int i = 0; i < length; i++) {
            month = month + random.nextInt(10);
        }
        return month;
    }

    public static String getYear(boolean isValid){
        int currentYear = LocalDate.now().getYear();
        int lastTwoDigits = currentYear % 100;
        if (LocalDate.now().getMonthValue() == 1 && !isValid) {
            lastTwoDigits = lastTwoDigits - 1;
        }
        return String.format("%02d", lastTwoDigits);
    }

    public static String getInvalidYearFormat(boolean isShort) {
        Random random = new Random();
        int length = isShort ? 1 : 3;
        String year = "";
        for (int i = 0; i < length; i++) {
            year = year + random.nextInt(10);
        }
        return year;
    }

    public static String getName(boolean isValid){
        Locale locale = isValid ? new Locale("en") : new Locale("ru");
        Faker faker = new Faker(locale);
        return faker.name().firstName() + " " + faker.name().lastName(); // or full?
    }

    public static String getInvalidNameFormat(boolean isShort) {
        Random random = new Random();
        int length = isShort ? random.nextInt(2) + 1 : random.nextInt(8) + 28;
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String getValidCvc(){
        Random random = new Random();
        int cvc = random.nextInt(1000);
        return String.format("%03d", cvc);
    }

    public static String getInvalidCvc(boolean isShort) {
        Random random = new Random();
        int length = isShort ? random.nextInt(1) + 1 : random.nextInt(9999) + 1000;
        return RandomStringUtils.randomNumeric(length);
    }
}
