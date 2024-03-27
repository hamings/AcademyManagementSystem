package src.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidationSystem {
    public static boolean isValidPassword(String input) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidId(String input) {
        String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidEmail(String input) {
        String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidPhoneNumber(String input) {
        String regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidDate(String inputDate) {
        // 년-월-일 형식 확인을 위한 정규 표현식
        String datePattern = "^(19\\d{2}|20\\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
        if (!inputDate.matches(datePattern)) {
            // 입력 형식이 년-월-일 패턴과 일치하지 않으면 false 반환
            return false;
        }

        String[] parts = inputDate.split("-");
        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (year < 1900 || year > 2099) return false;
            if (month < 1 || month > 12) return false;
            if (day < 1 || day > 31) return false;

            // 각 월의 최대 일수
            int[] daysInMonth = {
                    31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
            };

            // 윤년 체크
            if (month == 2 && isLeapYear(year)) {
                daysInMonth[1] = 29; // 2월의 경우, 윤년에는 29일까지 있음
            }

            return day <= daysInMonth[month - 1];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }

    public static boolean isValidaccountNumber(String input) {
        String regex = "^\\d{3,6}-\\d{2,6}-\\d+$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidName(String input) {
        String regex = "^[ㄱ-ㅎ가-힣]+$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidaccountPassword(String input) {
        String regex = "^\\d{4}$";
        boolean isValid = Pattern.matches(regex, input);
        if (!isValid) return false;
        return true;
    }

    public static boolean isValidGender(String input) {
        return input.equalsIgnoreCase("Female") || input.equalsIgnoreCase("Male");
    }
}
