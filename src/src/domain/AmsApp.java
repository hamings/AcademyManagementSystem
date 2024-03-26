package src.domain;

import src.ServiceType;
import src.repository.Repository;
import src.repository.RepositoryProvider;
import src.repository.TeacherRepository;
import src.service.AdminService;
import src.service.StudentService;
import src.service.TeacherService;
import src.service.UserService;
import src.util.ValidationSystem;

import java.io.IOException;
import java.util.Scanner;

public class AmsApp {
    private Scanner scanner;
    private StudentService studentService;
    private TeacherService teacherService;
    private AdminService adminService;
    private UserService userService;
    public AmsApp() throws IOException {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
        this.teacherService = new TeacherService();
        this.adminService = new AdminService();
        this.userService = new UserService();
    }
    public void run() throws IOException {
        do {
            System.out.println();
            //ams 오신것을 환영합니다 print
            System.out.println("********************");
            System.out.println("****** 효성AMS ******");
            System.out.println("********************");

            //1: 로그인, 2: 회원가입
            System.out.println("------[시작하기]------");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("--------------------");

            System.out.print("[입력]: ");
            String num = scanner.nextLine();
            System.out.println();
            if (num.equals("1")) {
                //1번이라면 1.학생, 2.강사, 3.관리자 중 선택하세요 출력

                System.out.println("-----[로그인 메뉴]-----");
                System.out.println("1. 학생 ");
                System.out.println("2. 강사 ");
                System.out.println("3. 관리자 ");
                System.out.println("----------------------");
                System.out.print("[입력]: ");
                //번호 선택해주세요
                num = scanner.nextLine();
                if(num.equals("1")||num.equals("2")||num.equals("3")) {
                    login(num);
                }else{
                    System.out.println("올바르지 않은 번호입니다.");
                    continue;
                }
                if (num.equals("1")) { //학생 서비스
                    while (studentService.getStudent()!=null) {
                        studentService.showStudentMenu();
                        num = scanner.nextLine();
                        switch (num) {
                            case "1": {
                                studentService.showRegistrationLectureMenu();
                                num = scanner.nextLine();
                                if(num.equals("1")) { //수강신청 선택
                                    studentService.showAllLectureList();
                                    // 원하는 강의 고르기
                                    System.out.print("수강하고자 하는 강의의 강의 id를 입력해주세요: \n");
                                    System.out.print("[입력]: ");
                                    String choiceLectureId = scanner.nextLine();
                                    studentService.registerLecture(choiceLectureId);
                                    break;
                                }else if (num.equals("2")){ //수강신청 취소
                                    if(studentService.showStudentAllRegistrationLecture()){
                                        System.out.println("취소하고자 하는 강의의 강의 id를 입력해주세요: \n");
                                        System.out.print("[입력]: ");
                                        String lectureId = scanner.nextLine();
                                        studentService.deleteLecture(lectureId);
                                    }

                                }else if(num.equals("3")){
                                    studentService.showStudentAllRegistrationLecture();
                                }else{
                                    //올바르지 않은 번호 선택
                                    System.out.println("\n올바르지 않은 번호입니다.");
                                }
                                break;
                            }
                            case "2": {
                                studentService.showStudyRoom();
                                studentService.showStudyRoomMenu();
                                num = scanner.nextLine();
                                if(num.equals("1")){
                                    if(!studentService.isExistResevation()) {
                                        System.out.println("\n예약을 원하는 좌석 번호를 입력하세요(ex: 1-1)");
                                        System.out.print("[입력]: ");
                                        String choiceSeat = scanner.nextLine();
                                        studentService.isPossibleReserve(choiceSeat);
                                    }
                                }else if(num.equals("2")){
                                    System.out.print("\n취소를 원하는 좌석 좌석 번호를 입력하세요(ex: 1-1)");
                                    System.out.print("[입력]: ");
                                    String seatNum = scanner.nextLine();
                                    studentService.cancelReservation(seatNum);
                                }else{
                                    System.out.println("\n올바르지 않은 번호입니다.\n");
                                }

                                break;
                            }
                            case "3": {
                                //알림 확인 로직
                                System.out.println();
                                studentService.checkNotification();
                                System.out.println();
                                break;
                            }
                            default: {
                                System.out.println("올바른 번호를 입력해주세요!");
                                break;
                                //번호 잘못입력
                            }
                        }

                    }
                } else if (num.equals("2")) { //강사 서비스
                    loop:
                    while (teacherService.getTeacher() != null) {
                        //강사 메뉴 1.강의 리스트, 2. 강의별 학생 리스트 3.로그아웃
                        System.out.println("----------[강사 서비스]-------------");
                        System.out.println("1. 강의 리스트");
                        System.out.println("2. 강의별 학생 리스트");
                        System.out.println("3. 로그아웃");
                        System.out.println("----------------------------------");
                        System.out.print("[입력]: ");
                        num = scanner.nextLine();
                        System.out.println();
                        switch (num) {
                            case "1": {
                                System.out.println("------------------------------------[담당강의 리스트]---------------------------------------");
                                teacherService.showLectureList();
                                System.out.println("-----------------------------------------------------------------------------------------");
                                System.out.println();
                                break;
                            }
                            case "2": {
                                //강의번호를 입력해주세요
                                System.out.print("[강의아이디를 입력해주세요]: ");
                                String lectureId = scanner.nextLine();
                                System.out.println();
                                teacherService.showStudentListByLecture(lectureId);
                                break;
                            }
                            case "3": {
                                //로그아웃함
                                teacherService.setTeacher((Teacher) userService.logout());
                                break loop;
                            }
                            default: {
                                //번호 잘못입력
                                System.out.println("올바르지 않은 번호입니다!");
                            }
                        }

                    }
                } else if (num.equals("3")) { //관리자 서비스
                    while (adminService.getAdmin() != null) {
                        System.out.println("----------[관리자 서비스]------------");
                        System.out.println("1. 학생관리");
                        System.out.println("2. 강사관리");
                        System.out.println("3. 강의관리");
                        System.out.println("4. 납부관리");
                        System.out.println("5. 로그아웃");
                        System.out.println("----------------------------------");
                        System.out.print("[입력]: ");
                        num = scanner.nextLine();

                        switch (num) {
                            case "1": {
                                adminService.showStudentList();
                                System.out.println();
                                System.out.println("----------[학생정보관리]------------");
                                System.out.println("1. 학생상세정보");
                                System.out.println("2. 학생정보수정");
                                System.out.println("3. 학생정보삭제");
                                System.out.println("---------------------------------");

                                System.out.print("[입력]: ");
                                num = scanner.nextLine();
                                if (num.equals("1")) { //학생 상세
                                    showDetailStudent();
                                } else if (num.equals("2")) { //학생 수정
                                    editStudentInformation();
                                } else if (num.equals("3")) { //학생 삭제
                                    deleteStudentInformation();
                                } else{
                                    System.out.println("올바른 번호를 입력해주세요!");
                                }
                                break;
                            }
                            case "2": {
                                adminService.showTeacherList();
                                System.out.println();
                                System.out.println("----------[강사정보관리]------------");
                                System.out.println("1. 강사상세정보");
                                System.out.println("2. 강사정보수정");
                                System.out.println("---------------------------------");
                                System.out.print("[입력]: ");
                                num = scanner.nextLine();

                                if (num.equals("1")) { //강사 상세
                                    showDetailTeacher();
                                } else if (num.equals("2")) { //강사 수정
                                    editTeacherInformation();}
//                                } else if (num.equals("3")) { //강사 삭제
//                                    deleteTeacherInformation();
//                                }
                                else {
                                    System.out.println("잘못된 번호를 입력하여 메인메뉴로 돌아갑니다.");
                                }
                                break;
                            }
                            case "3": {
                                adminService.showLectureList();
                                System.out.println();
                                System.out.println("----------[강의정보관리]------------");
                                System.out.println("1. 강의상세정보");
                                System.out.println("2. 신규강의등록");
                                System.out.println("3. 강의정보삭제");
                                System.out.println("---------------------------------");
                                System.out.print("[입력]: ");
                                num = scanner.nextLine();

                                if (num.equals("1")) { //강의 상세
                                    showDetailLecture();
                                } else if (num.equals("2")) { //강의 등록
                                    registerLecture();
                                } else if (num.equals("3")) { //강의 삭제
                                    deleteLectureInformation();
                                } else { //숫자 잘못 입력
                                    System.out.println("올바르지 않은 번호입니다.");
                                }
                                break;
                            }
                            case "4": {
                                adminService.bankSystem();
                                break;
                            }
                            case "5": {
                                //로그아웃
                                System.out.println("[로그아웃] 되셨습니다!");
                                adminService.setAdmin((Admin) userService.logout());
                            } default:
                                break;
                        }

                    }
                }
            }
            else if (num.equals("2")) {
                    signIn();
            } else {
                    System.out.println("올바른 번호를 다시 입력해주세요!");
            }

        }while (true) ;

    }
    public void login(String choiceNum) throws IOException {

        System.out.println("***********************");
        //id input
        System.out.print("[아이디 입력]: ");
        String id = scanner.nextLine();
        //pw input
        System.out.print("[비밀번호 입력]: ");
        String password = scanner.nextLine();
        System.out.println("***********************");

        if(choiceNum.equals("1")){
            studentService.setStudent(userService.loginStudent(id,password));
        }else if (choiceNum.equals("2")){
            teacherService.setTeacher(userService.loginTeacher(id,password));
        }else if(choiceNum.equals("3")){
            adminService.setAdmin(userService.loginAdmin(id,password));;
        }else{
            System.out.println("올바른 번호를 입력해주세요!");
        }

    }
    public void signIn() throws IOException {
        System.out.println("-------------[회원가입]-------------");
        System.out.println("[효성AMS에 오신것을 진심으로 환영합니다!]");
        System.out.println("1. 학생으로 회원가입");
        System.out.println("2. 강사로 회원가입");
        System.out.println("----------------------------------");
        System.out.print("[입력]: ");
        String choiceNum = scanner.nextLine();
        if(!(choiceNum.equals("1")||choiceNum.equals("2"))){
            System.out.println("올바르지 않은 번호 입니다.");
            return;
        }
        System.out.println("id: ");
        System.out.println("-----------[아이디 형식]------------");
        System.out.println("1. 아이디의 시작은 영문으로만 가능합니다.");
        System.out.println("2. '_'를 제외한 특수문자는 불가능합니다.");
        System.out.println("3. 아이디의 자리수는 5~12자 이내로 가능합니다.");
        System.out.println("----------------------------------");
        System.out.print("[아이디]: ");
        String id = scanner.nextLine();
        while (!ValidationSystem.isValidId(id) || userService.isExistStudent(id) || userService.isExistTeacher(id)) {
            //id 오류 출력
            // 예) 시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
            System.out.println("아이디형식이 잘못 되셨습니다!");
            System.out.print("[재입력]: ");
            id = scanner.nextLine();
        }
        System.out.println("----------[비밀번호 형식]------------");
        System.out.println("1. 최소 1개이상의 영어 대문자");
        System.out.println("2. 최소 1개이상의 영어 소문자");
        System.out.println("3. 최소 1개이상의 숫자");
        System.out.println("4. 최소 1개이상의 특수문자");
        System.out.println("5. 최소 8자리 이상의 비밀번호");
        System.out.println("----------------------------------");
        System.out.print("[비밀번호]: ");
        String password = scanner.nextLine();
        while (!ValidationSystem.isValidPassword(password)) {
            //pw 오류
            System.out.println("비밀번호형식이 올바르지 않습니다!");
            System.out.print("[재입력]: ");
            password = scanner.nextLine();
        }
        System.out.print("[이름]: ");
        String name = scanner.nextLine();
        while (!ValidationSystem.isValidName(name)) {
            //name 오류 출력
            System.out.print("[재입력]: ");
            name = scanner.nextLine();
        }
        System.out.println("------------[성별 형식]-------------");
        System.out.println("1. 남자: M(m)ale");
        System.out.println("2. 여자: F(f)emale");
        System.out.println("----------------------------------");
        System.out.print("[성별]: ");
        String gender = scanner.nextLine();
        while (!ValidationSystem.isValidGender(gender)) {
            System.out.println("성별형식이 올바르지 않습니다!");
            System.out.print("[재입력]: ");
            gender = scanner.nextLine();
        }
        System.out.println("----------[휴대폰번호 형식]-----------");
        System.out.println("1. 앞자리는 2~3자리의 숫자로 입력해주세요.");
        System.out.println("2. 앞자리의 중간자리 사이에 '-'을 입력해주세요.");
        System.out.println("3. 중간자리는 3~4자리의 숫자로 입력해주세요.");
        System.out.println("4. 중간자리와 뒷자리 사이에 '-'을 입력해주세요.");
        System.out.println("5. 마지막자리는 4자리의 숫자로 입력해주세요.");
        System.out.println("-----------------------------------");
        System.out.print("[휴대폰번호]: ");
        String phoneNumber = scanner.nextLine();
        while (!ValidationSystem.isValidPhoneNumber(phoneNumber)) {
            //phone 오류
            System.out.println("휴대폰번호형식이 올바르지 않습니다!");
            System.out.print("[재입력]: ");
            phoneNumber = scanner.nextLine();
        }
        System.out.println("----------[생년월일 형식]-----------");
        System.out.println("1. 1900년부터 2024년까지의 연도를 입력해주세요.");
        System.out.println("2. '-'으로 년도와 월을 구분해 주세요.");
        System.out.println("3. 01월부터 12월까지의 월을 입력해주세요.");
        System.out.println("4. '-'으로 월과 일을 구분해 주세요.");
        System.out.println("5. 01일부터 31일까지 일을 입력해주세요.");
        System.out.println("-----------------------------------");
        System.out.print("[생년월일]: ");
        String birthday = scanner.nextLine();
        while (!ValidationSystem.isValidDate(birthday)) {
            //birth 오류 출력
            System.out.println("생년월일형식이 올바르지 않습니다!");
            System.out.print("[재입력]: ");
            birthday = scanner.nextLine();
        }
        if(choiceNum.equals("1")) {
            System.out.println("----------[계좌번호 형식]-----------");
            System.out.println("1. 첫 번째 부분은 3자리에서 6자리 사이의 숫자입니다.");
            System.out.println("2. 두 번째 부분은 2자리에서 6자리 사이의 숫자입니다.");
            System.out.println("3. 세 번째 부분은 1자리 이상의 숫자입니다.");
            System.out.println("4. 각 부분은 하이픈(-)으로 구분됩니다.");
            System.out.println("----------------------------------");
            System.out.print("[계좌번호]: ");
            String accountNumber = scanner.nextLine();
            while (!ValidationSystem.isValidaccountNumber(accountNumber)) {
                //accountNumber 오류
                System.out.println("계좌번호형식이 올바르지 않습니다!");
                System.out.print("[재입력]: ");
                accountNumber = scanner.nextLine();
            }
            System.out.println("----------[계좌비밀번호 형식]-----------");
            System.out.println("1. 숫자로 구성된 4자리수여야 합니다.");
            System.out.println("-----------------------------------");
            System.out.print("[계좌비밀번호]: ");
            String accountPassword = scanner.nextLine();
            while (!ValidationSystem.isValidaccountPassword(accountPassword)) {
                //accountpw 오류
                System.out.println("계좌비밀번호형식이 올바르지 않습니다!");
                System.out.print("[재입력]: ");
                accountPassword = scanner.nextLine();
            }
            System.out.println("[효성AMS의 학생으로 회원등록되셨습니다!]");
            System.out.println("************감사합니다^^************");
            userService.signInStudent(id, password, name, gender, phoneNumber, birthday, accountNumber, accountPassword);
        } else if (choiceNum.equals("2")) {
            System.out.println("----------[이메일 형식]-----------");
            System.out.println("1. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
            System.out.println("2. 반드시 '@'문자가 있어야 합니다.");
            System.out.println("3. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
            System.out.println("4. 반드시 '.'문자가 있어야 합니다.");
            System.out.println("5. 최소 한 개 이상의 소문자 알파벳을 포함해야 합니다.");
            System.out.println("-----------------------------------");
            System.out.print("이메일: ");
            String email = scanner.nextLine();
            while (!ValidationSystem.isValidEmail(email)) {
                //email 오류
                System.out.print("[재입력]: ");
                email = scanner.nextLine();
            }
            System.out.println("[효성AMS의 강사로 회원등록되셨습니다!]");
            System.out.println("***********감사합니다^^***********");
            userService.signInTeacher(id,password,name,gender,phoneNumber,birthday,email);
        }

    }
    public void showDetailStudent() throws IOException {
        System.out.println("**********************************");
        String studentId;
        while(true) {
            System.out.println("[상세정보를 확인하실 학생의 아이디]");
            System.out.print("[입력]: ");
            studentId = scanner.nextLine();
            System.out.println("**********************************");
            if(adminService.detailStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    public void editStudentInformation() throws IOException {
        System.out.println("**********************************");
        String studentId;

        while(true) {
            System.out.println("[수정하실 학생의 아이디]");
            System.out.print("[입력]: ");
            studentId = scanner.nextLine();
            System.out.println("**********************************");
            if(adminService.showStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴

        // 메뉴 출력
        String value = "";
        int option = 0;
        while(true) {
            System.out.print("[입력]: ");
            String input = scanner.nextLine();
            System.out.println("**********************************");
            // 입력 값 검증
            if (input.isEmpty()) {
                System.out.print("올바른 값을 입력해주세요: ");
                continue;
            }

            try {
                option = Integer.parseInt(input);
                if(0 < option && option < 5) break; // 올바른 범위의 숫자가 입력된 경우
                else System.out.println("1 ~ 4 사이의 번호를 입력해주세요!");
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요!"); // 숫자가 아닌 값을 입력한 경우
            }
        }

        switch(option) {
            case 1 :
                while(true) {
                    System.out.println("[새로운 비밀번호]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식이 올바르지 않습니다!");
                        System.out.println("-------[비밀번호 형식]-------");
                        System.out.println("1. 최소 1개이상의 영어 대문자");
                        System.out.println("2. 최소 1개이상의 영어 소문자");
                        System.out.println("3. 최소 1개이상의 숫자");
                        System.out.println("4. 최소 1개이상의 특수문자");
                        System.out.println("5. 최소 8자리 이상의 비밀번호");
                        System.out.println("---------------------------");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.println("[새로운 이름]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.println("[새로운 휴대폰번호] ");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다!");
                        System.out.println("----------[휴대폰번호 형식]-----------");
                        System.out.println("1. 앞자리는 2~3자리의 숫자로 입력해주세요.");
                        System.out.println("2. 앞자리의 중간자리 사이에 '-'을 입력해주세요.");
                        System.out.println("3. 중간자리는 3~4자리의 숫자로 입력해주세요.");
                        System.out.println("4. 중간자리와 뒷자리 사이에 '-'을 입력해주세요.");
                        System.out.println("5. 마지막자리는 4자리의 숫자로 입력해주세요.");
                        System.out.println("-----------------------------------");

                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.println("[새로운 계좌비밀번호]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidaccountPassword(value)){
                        System.out.println("계좌비밀번호형식이 잘못 되셨습니다1");
                        System.out.println("----------[계좌비밀번호 형식]-----------");
                        System.out.println("1. 숫자로 구성된 4자리수여야 합니다.");
                        System.out.println("-----------------------------------");
                    } else break;
                }
                break;
        }

        adminService.newEditStudentInformation(studentId, option, value);
    }
    //학생정보삭제
    public void deleteStudentInformation() throws IOException {
        System.out.println("**********************************");
        String studentId;

        while(true) {
            System.out.println("[삭제하실 학생의 아이디]");
            System.out.print("[입력]: ");
            studentId = scanner.nextLine();
            System.out.println("*****************************************");
            if(adminService.newDeleteStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    //강사정보 상세보기
    public void showDetailTeacher() throws IOException {
        System.out.println("*********************************");
        String teacherId;

        while(true) {
            System.out.println("[상세정보를 확인하실 강사의 아이디]");
            System.out.print("[입력]: ");
            teacherId = scanner.nextLine();
            System.out.println("*****************************************");
            if(adminService.detailTeacherInformation(teacherId))
                System.out.println();
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    //강사정보수정
    public void editTeacherInformation() throws IOException {
        System.out.println("**********************************");
        String teacherId;

        while(true) {
            System.out.println("[수정하실 강사의 아이디]: ");
            System.out.print("[입력]: ");
            teacherId = scanner.nextLine();
            System.out.println("*********************************************");
            if(adminService.showTeacherInformation(teacherId))break; // 수정할 학생 찾음
        } // 선생 찾아옴

        // 메뉴 출력

        String value = "";
        int option = 0;
        while(true) {
            System.out.println("[수정하실 번호]");
            System.out.print("[입력]: ");
            option = Integer.parseInt(scanner.nextLine());

            if(0 < option && option < 5) break;
            else System.out.println("1 ~ 4 사이의 번호를 입력해주세요.");
        }

        switch(option) {
            case 1 :
                while(true) {
                    System.out.println("[새로운 비밀번호]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식이 올바르지 않습니다!");
                        System.out.println("-------[비밀번호 형식]-------");
                        System.out.println("1. 최소 1개이상의 영어 대문자");
                        System.out.println("2. 최소 1개이상의 영어 소문자");
                        System.out.println("3. 최소 1개이상의 숫자");
                        System.out.println("4. 최소 1개이상의 특수문자");
                        System.out.println("5. 최소 8자리 이상의 비밀번호");
                        System.out.println("---------------------------");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.println("[새로운 이름]");
                    System.out.println("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.println("[새로운 휴대폰번호]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다.");
                        System.out.println("-----------[휴대폰번호 형식]-------------");
                        System.out.println("1. 앞자리는 2~3자리의 숫자로 입력해주세요.");
                        System.out.println("2. 앞자리의 중간자리 사이에 '-'을 입력해주세요.");
                        System.out.println("3. 중간자리는 3~4자리의 숫자로 입력해주세요.");
                        System.out.println("4. 중간자리와 뒷자리 사이에 '-'을 입력해주세요.");
                        System.out.println("5. 마지막자리는 4자리의 숫자로 입력해주세요.");
                        System.out.println("--------------------------------------");
                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.println("[새로운 이메일]");
                    System.out.print("[입력]: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidEmail(value)){
                        System.out.println("이메일형식이 잘못 되셨습니다!");
                        System.out.println("-----------[이메일 형식]-------------");
                        System.out.println("1. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
                        System.out.println("2. 반드시 '@'문자가 있어야 합니다.");
                        System.out.println("3. 최소 한 개 이상의 알파벳(소문자 또는 대문자) 또는 숫자가 있어야 합니다.");
                        System.out.println("4. 반드시 '.'문자가 있어야 합니다.");
                        System.out.println("5. 최소 한 개 이상의 소문자 알파벳을 포함해야 합니다.");
                        System.out.println("--------------------------------------");
                    } else break;
                }
                break;
        }

        adminService.newEditTeacherInformation(teacherId, option, value);
    }

    //강사정보삭제
    public void deleteTeacherInformation() throws IOException {
        System.out.println("**********************************");
        String teacherId;

        while(true) {
            System.out.println("[삭제하실 강사의 아이디]");
            System.out.print("[입력]: ");
            teacherId = scanner.nextLine();
            System.out.println("**********************************");
            if(adminService.newDeleteTeacherInformation(teacherId))
                break;
        }
    }
    //강의정보 상세보기
    public void showDetailLecture() throws IOException {
        System.out.println("**********************************");
        String lectureId;

        while(true) {
            System.out.println("[상세정보를 확인하실 강의 아이디]");
            System.out.print("[입력]: ");
            lectureId = scanner.nextLine();
            System.out.println("**********************************");
            if(adminService.detailLectureInformation(lectureId))
                break;
        }
    }
    //강의등록
    public void registerLecture() throws IOException {
        System.out.println("         [신규 강의정보 등록]        ");

        System.out.print("[강의 이름]: ");
        String name = scanner.nextLine();

        System.out.println("[강의 요일]");
        System.out.println("[0]: 월요일 [1]: 화요일 [2]: 수요일 [3]: 목요일 [4]: 금요일");
        System.out.print("[입력]: ");
        int day = -1;
        boolean dayCheck = false;
        while(!dayCheck){
            int intputDay = Integer.parseInt(scanner.nextLine());
            if(0<=intputDay && intputDay<5){
                day = intputDay;
                dayCheck = true;
                break;
            } else{
                System.out.print("올바른 번호를 입력해주세요: ");
            }
        }

        System.out.println("[강의 시간]");
        System.out.println("[0] 10:00 ~ 12:00 [1] 13:00 ~ 14:50 [2] 15:00 ~ 16:50 [3] 17:00 ~ 19:00");
        System.out.print("[입력]: ");
        int time = -1;
        boolean timeCheck = false;
        while(!timeCheck){
            int inputTime = Integer.parseInt(scanner.nextLine());
            if(0<=inputTime && inputTime<4){
                time = inputTime;
                timeCheck = true;
                break;
            } else{
                System.out.print("올바른 번호를 입력해주세요: ");
            }
        }

        String teacherId = "";
        String teacherName = "";
        boolean isTeacherExist = false;
        boolean isExistLecture = adminService.isExistTeacher(teacherId);

//        // 해당 시간에 이미 강의가 있는지 확인
//        if (isExist) {
//            System.out.println("강사가 이미 같은 요일과 같은 시간에 강의를 가지고 있습니다.");
//        }

        adminService.showTeacherList();
        while (!isTeacherExist) {
            System.out.println("[담당 강사 아이디]");
            System.out.print("[입력]: ");
            teacherId = scanner.nextLine();
            isTeacherExist = adminService.isExistTeacher(teacherId);
            // lectureRepository있으면,
            // teacherFound = lectureRepository.isExist(teacherId);

            if (isTeacherExist) {
                // 해당 시간과 요일에 이미 강의가 있는지 확인
                if (adminService.isExistSameTimeLecture(teacherId, day, time)) {
                    System.out.println("강사가 이미 같은 요일과 같은 시간에 강의를 가지고 있습니다.");
                    return;
                }
                teacherName = adminService.getTeacherName(teacherId);
                adminService.registerLecture(name, day, time, teacherName, teacherId);
                System.out.println("강의가 성공적으로 등록되었습니다!");
            } else {
                System.out.println("해당 아이디의 강사님은 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }
    //강의정보삭제
    public void deleteLectureInformation() throws IOException {
        System.out.println("**********************************");
        String lectureId;

        while(true) {
            System.out.println("[삭제하실 강의 아이디]");
            System.out.print("[입력]: ");
            lectureId = scanner.nextLine();
            if(adminService.newDeleteLectureInformation(lectureId))
                break;
        }
    }


}
