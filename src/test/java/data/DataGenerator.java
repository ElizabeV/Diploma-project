package data;

import com.github.javafaker.Faker;
import lombok.var;

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

    public static String getMonth(boolean isValid) {
        int currentMonth = LocalDate.now().getMonthValue();
        int month = isValid ? currentMonth : currentMonth - 1;
        return String.format("%02d", month);

    }

    public static String getYear(boolean isValid){
        int currentYear = LocalDate.now().getYear();
        int year = isValid ? currentYear : currentYear - 1;
        return String.format("%02d", year);
    }

    public static String getName(boolean isValid){
        Locale locale = isValid ? new Locale("en") : new Locale("ru");
        Faker faker = new Faker(locale);
        return faker.name().firstName() + " " + faker.name().lastName(); // or full?
    }

    public static String getValidCvc(){
        Random random = new Random();
        int cvc = random.nextInt(1000);
        return String.format("%03d", cvc);
    }

    public static String getInvalidCvc() {
        Random random = new Random();
        int cvc = random.nextInt(10000);
        return String.format("%04d", cvc);
    }
}
