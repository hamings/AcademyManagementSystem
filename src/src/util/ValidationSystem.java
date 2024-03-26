package src.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.regex.Pattern;

public class ValidationSystem {
    public static boolean isValidPassword (String input) {
        /*
        [비밀번호 형식]
        1. 소문자 최소하나
        2. 대문자 최소하나
        3. 숫자 최소하나
        4. 특수문자 최소하나
        5. 최소 8자리 이상
        */
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidId (String input) {
        String regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
        // 예) 시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidEmail (String input) {
        /*
        ^: 문자열의 시작을 나타냅니다.
        [a-zA-Z0-9]+: 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자를 의미합니다. 이 부분은 이메일의 사용자 이름 부분에 해당합니다.
        @: 리터럴 '@' 문자가 반드시 있어야 합니다. 이메일 주소에서 사용자 이름과 도메인을 구분하는 데 사용됩니다.
        [a-zA-Z0-9]+: '@' 기호 뒤에 오는 도메인 이름 부분으로, 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자를 포함해야 합니다.
        \\.: 리터럴 '.'(점) 문자입니다. 이메일 주소에서 도메인 이름과 최상위 도메인을 구분하는 데 사용됩니다. 자바에서는 백슬래시(\)를 이스케이프 문자로 사용하기 때문에 점을 나타내기 위해 \\.를 사용합니다.
        [a-z]+: 도메인 이름 뒤에 오는 최상위 도메인 부분으로, 최소 한 개 이상의 소문자 알파벳을 포함해야 합니다.
        $: 문자열의 끝을 나타냅니다.
         */
        String regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+$";

        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidPhoneNumber (String input) {
        String regex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

//    public static boolean isValidBirthDay (String input) {
//        String regex = "^(19[0-9][0-9]|20[0-1][0-9]|202[0-4])-?(0[1-9]|1[0-2])-?(0[1-9]|[1-2][0-9]|3[0-1])$";
//        // 주의] 월별 일자는 체크 못함
//        boolean isValid = Pattern.matches(regex, input);
//        if(!isValid) return false;
//
//        try{
//            SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
//            dateFormat.setLenient(false);
//            dateFormat.parse(input);
//            return  true;
//        }catch (ParseException e){
//            return  false;
//        }
//    }

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

    public static boolean isValidaccountNumber (String input) {
        /*
        첫 번째 부분은 3자리에서 6자리 사이의 숫자입니다.
        두 번째 부분은 2자리에서 6자리 사이의 숫자입니다.
        세 번째 부분은 1자리 이상의 숫자입니다.
        각 부분은 하이픈(-)으로 구분됩니다.
         */
        String regex = "^\\d{3,6}-\\d{2,6}-\\d+$";
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidName (String input) {
        String regex = "^[ㄱ-ㅎ가-힣]+$";

        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidaccountPassword (String input) {
        String regex = "^\\d{4}$";
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }
    public static boolean isValidGender(String input){
        return input.equalsIgnoreCase("Female") ||input.equalsIgnoreCase("Male");
    }

}
