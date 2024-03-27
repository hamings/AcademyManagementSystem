package src.util;

public class Constant {
    //Menu print
    public static void printMainMenu() {
        System.out.println();
        System.out.println("********************");
        System.out.println("****** 효성AMS ******");
        System.out.println("********************");
    }

    public static void printLoginOrSignInMenu() {
        System.out.println("------[시작하기]------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("--------------------");
    }

    public static void printLoginMenu() {
        System.out.println("-----[로그인 메뉴]-----");
        System.out.println("1. 학생 ");
        System.out.println("2. 강사 ");
        System.out.println("3. 관리자 ");
        System.out.println("----------------------");
    }

    public static void printStudentMenu() {
        System.out.println("-----------[학생 관리]-----------");
        System.out.println("1. 수강 관리");
        System.out.println("2. 자습실 예약");
        System.out.println("3. 납부 알림 확인");
        System.out.println("4. 로그아웃");
        System.out.println("-------------------------------\n");
        System.out.println("원하시는 서비스 번호를 선택하세요");
    }

    public static void printRegistrationLectureMenu() {
        System.out.println("\n---------[수강 관리 시스템]---------");
        System.out.println("1. 수강 신청");
        System.out.println("2. 수강 취소");
        System.out.println("3. 수강 신청 내역");
        System.out.println("0. 뒤로 가기");
        System.out.println("----------------------------------\n");
        System.out.println("원하시는 서비스 번호를 선택하세요");
    }

    public static void printStudyRoomMenu() {
        System.out.println("---------[자습실 관리 시스템]---------");
        System.out.println("1. 좌석 예약하기");
        System.out.println("2. 좌석 취소하기");
        System.out.println("0. 뒤로 가기");
        System.out.println("-----------------------------------\n");
        System.out.println("원하시는 서비스 번호를 선택하세요");
    }

    public static void printTeacherMenu() {
        System.out.println("----------[강사 서비스]-------------");
        System.out.println("1. 강의 리스트");
        System.out.println("2. 강의별 학생 리스트");
        System.out.println("3. 로그아웃");
        System.out.println("----------------------------------");
    }

    public static void printAdminMenu() {
        System.out.println("----------[관리자 서비스]------------");
        System.out.println("1. 학생관리");
        System.out.println("2. 강사관리");
        System.out.println("3. 강의관리");
        System.out.println("4. 납부관리");
        System.out.println("5. 로그아웃");
        System.out.println("----------------------------------");
    }

    public static void printStudentInformationMenu() {
        System.out.println();
        System.out.println("----------[학생정보관리]------------");
        System.out.println("1. 학생상세정보");
        System.out.println("2. 학생정보수정");
        System.out.println("3. 학생정보삭제");
        System.out.println("0. 뒤로 가기");
        System.out.println("---------------------------------");
    }

    public static void printTeacherInformationMenu() {
        System.out.println();
        System.out.println("----------[강사정보관리]------------");
        System.out.println("1. 강사상세정보");
        System.out.println("2. 강사정보수정");
        System.out.println("0. 뒤로 가기");
        System.out.println("---------------------------------");
    }

    public static void printLectureInfromationMenu() {
        System.out.println();
        System.out.println("----------[강의정보관리]------------");
        System.out.println("1. 강의상세정보");
        System.out.println("2. 신규강의등록");
        System.out.println("3. 강의정보삭제");
        System.out.println("0. 뒤로 가기");
        System.out.println("---------------------------------");
    }

    public static void printSignInMenu() {
        System.out.println("-------------[회원가입]-------------");
        System.out.println("[효성AMS에 오신것을 진심으로 환영합니다!]");
        System.out.println("1. 학생으로 회원가입");
        System.out.println("2. 강사로 회원가입");
        System.out.println("----------------------------------");
    }

    //회원가입 속성 형식
    public static void printIdFormat() {
        System.out.println("-----------[아이디 형식]------------");
        System.out.println("1. 아이디의 시작은 영문으로만 가능합니다.");
        System.out.println("2. '_'를 제외한 특수문자는 불가능합니다.");
        System.out.println("3. 아이디의 자리수는 5~12자 이내로 가능합니다.");
        System.out.println("----------------------------------");
    }

    public static void printPasswordFormat() {
        System.out.println("----------[비밀번호 형식]------------");
        System.out.println("1. 최소 1개이상의 영어 대문자");
        System.out.println("2. 최소 1개이상의 영어 소문자");
        System.out.println("3. 최소 1개이상의 숫자");
        System.out.println("4. 최소 1개이상의 특수문자");
        System.out.println("5. 최소 8자리 이상의 비밀번호");
        System.out.println("----------------------------------");
    }

    public static void printGenderFormat() {
        System.out.println("------------[성별 형식]-------------");
        System.out.println("1. 남자: M(m)ale");
        System.out.println("2. 여자: F(f)emale");
        System.out.println("----------------------------------");
    }

    public static void printPhoneNumberFormat() {
        System.out.println("----------[휴대폰번호 형식]-----------");
        System.out.println("1. 앞자리는 2~3자리의 숫자로 입력해주세요.");
        System.out.println("2. 앞자리의 중간자리 사이에 '-'을 입력해주세요.");
        System.out.println("3. 중간자리는 3~4자리의 숫자로 입력해주세요.");
        System.out.println("4. 중간자리와 뒷자리 사이에 '-'을 입력해주세요.");
        System.out.println("5. 마지막자리는 4자리의 숫자로 입력해주세요.");
        System.out.println("-----------------------------------");
    }

    public static void printBirthFormat() {
        System.out.println("----------[생년월일 형식]-----------");
        System.out.println("1. 1900년부터 2024년까지의 연도를 입력해주세요.");
        System.out.println("2. '-'으로 년도와 월을 구분해 주세요.");
        System.out.println("3. 01월부터 12월까지의 월을 입력해주세요.");
        System.out.println("4. '-'으로 월과 일을 구분해 주세요.");
        System.out.println("5. 01일부터 31일까지 일을 입력해주세요.");
        System.out.println("-----------------------------------");
    }

    public static void printAccountNumberFormat() {
        System.out.println("----------[계좌번호 형식]-----------");
        System.out.println("1. 첫 번째 부분은 3자리에서 6자리 사이의 숫자입니다.");
        System.out.println("2. 두 번째 부분은 2자리에서 6자리 사이의 숫자입니다.");
        System.out.println("3. 세 번째 부분은 1자리 이상의 숫자입니다.");
        System.out.println("4. 각 부분은 하이픈(-)으로 구분됩니다.");
        System.out.println("----------------------------------");
    }

    public static void printAccountPasswordFormat() {
        System.out.println("----------[계좌비밀번호 형식]-----------");
        System.out.println("1. 숫자로 구성된 4자리수여야 합니다.");
        System.out.println("-----------------------------------");
    }

    public static void printEmailFormat() {
        System.out.println("----------[이메일 형식]-----------");
        System.out.println("1. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
        System.out.println("2. 반드시 '@'문자가 있어야 합니다.");
        System.out.println("3. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
        System.out.println("4. 반드시 '.'문자가 있어야 합니다.");
        System.out.println("5. 최소 한 개 이상의 소문자 알파벳을 포함해야 합니다.");
        System.out.println("-----------------------------------");
    }

    //////////////////////////////////////
    public static void printInputText() {
        System.out.print("[입력]: ");
    }

    public static void incorrectNumber() {
        System.out.println("올바르지 않은 번호입니다.");
        System.out.println();
    }

    public static void printLogout() {
        System.out.println("[로그아웃] 되셨습니다!");
    }

    public static void reInput() {
        System.out.print("[재입력]: ");
    }

    public static void notMatchFormat() {
        System.out.println("형식이 올바르지 않습니다!");
    }


}
