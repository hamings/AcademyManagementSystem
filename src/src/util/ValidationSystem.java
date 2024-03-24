package src.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class ValidationSystem {
    public static boolean isValidPassword (String input) {
        String regex = "^.*(?=^.{4,9}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";

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

    public static boolean isValidBirthDay (String input) {
        String regex = "^(19[0-9][0-9]|20[0-1][0-9]|202[0-4])-?(0[1-9]|1[0-2])-?(0[1-9]|[1-2][0-9]|3[0-1])$";
        // 주의] 월별 일자는 체크 못함
        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;

        try{
            SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            dateFormat.parse(input);
            return  true;
        }catch (ParseException e){
            return  false;
        }
    }

    public static boolean isValidaccountNumber (String input) {
        return false;
    }

    public static boolean isValidName (String input) {
        String regex = "^[ㄱ-ㅎ가-힣]+$";

        boolean isValid = Pattern.matches(regex, input);
        if(!isValid) return false;
        return true;
    }

    public static boolean isValidaccountPassword (String input) {
        return false;
    }

}
