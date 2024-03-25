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
            //ams 오신것을 환영합니다 print
            System.out.println("ams");

            //1: 로그인, 2: 회원가입
            System.out.println("1: 로그인, 2: 회원가입");

            String num = scanner.nextLine();
            if (num.equals("1")) {
                //1번이라면 1.학생, 2.강사, 3.관리자 중 선택하세요 출력
                System.out.println("1.학생, 2.강사, 3.관리자  \n 번호를 선택하세요");
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
                                System.out.println("1.수강신청 2.수강취소 3.수강신청내역");
                                num = scanner.nextLine();
                                if(num.equals("1")) { //수강신청 선택
                                    studentService.showAllLectureList();
                                    // 원하는 강의 고르기
                                    System.out.print("수강하고자 하는 강의의 강의 id를 입력해주세요: ");
                                    String choiceLectureId = scanner.nextLine();
                                    studentService.registerLecture(choiceLectureId);
                                    break;
                                }else if (num.equals("2")){ //수강신청 취소
                                    if(studentService.showStudentAllRegistrationLecture()){
                                        System.out.println("취소하고자 하는 강의의 강의 id를 입력해주세요: ");
                                        String lectureId = scanner.nextLine();
                                        studentService.deleteLecture(lectureId);
                                    }

                                }else if(num.equals("3")){
                                    studentService.showStudentAllRegistrationLecture();
                                }else{
                                    //올바르지 않은 번호 선택
                                    System.out.println("올바르지 않은 번호입니다.");
                                }
                                break;
                            }
                            case "2": {
                                studentService.showStudyRoom();
                                studentService.showStudyRoomMenu();
                                num = scanner.nextLine();
                                if(num.equals("1")){
                                    if(!studentService.isExistResevation()) {
                                        System.out.print("예약을 원하는 좌석 번호를 입력하세요(ex: 1-1) : ");
                                        String choiceSeat = scanner.nextLine();
                                        studentService.isPossibleReserve(choiceSeat);
                                    }
                                }else if(num.equals("2")){
                                    System.out.print("취소를 원하는 좌석 좌석 번호를 입력하세요(ex: 1-1) : ");
                                    String seatNum = scanner.nextLine();
                                    studentService.cancelReservation(seatNum);
                                }else{
                                    System.out.println("올바르지 않은 번호입니다.");
                                }

                                break;
                            }
                            case "3": {
                                //알림 확인 로직
                                studentService.checkNotification();
                                break;

                            }
                            default: {
                                //번호 잘못입력
                                System.out.println("올바르지 않은 번호입니다.");
                            }
                        }

                    }
                } else if (num.equals("2")) { //강사 서비스
                    loop:
                    while (teacherService.getTeacher() != null) {
                        //강사 메뉴 1.강의 리스트, 2. 강의별 학생 리스트 3.로그아웃
                        System.out.println("1.강의 리스트, 2. 강의별 학생 리스트 3.로그아웃");
                        num = scanner.nextLine();
                        switch (num) {
                            case "1": {
                                teacherService.showLectureList();
                                break;
                            }
                            case "2": {
                                //강의번호를 입력해주세요
                                System.out.println("강의번호를 입력해주세요.");
                                String lectureNumber = scanner.nextLine();
                                teacherService.showStudentListByLecture(lectureNumber);
                                break;
                            }
                            case "3": {
                                //로그아웃함
                                teacherService.setTeacher((Teacher) userService.logout());
                                break loop;
                            }
                            default: {
                                //번호 잘못입력
                                System.out.println("올바르지 않은 번호입니다.");
                            }
                        }

                    }
                } else if (num.equals("3")) { //관리자 서비스
                    while (adminService.getAdmin() != null) {
                        System.out.println("1.학생관리 2.강사관리 3.강의관리 4.납부관리 5.로그아웃");
                        System.out.println("번호 입력해주세요.");
                        num = scanner.nextLine();
                        switch (num) {
                            case "1": {
                                adminService.showStudentList();
                                System.out.println("1.학생상세 2.학생수정 3.학생삭제");
                                num = scanner.nextLine();
                                if (num.equals("1")) { //학생 상세
                                    showDetailStudent();
                                } else if (num.equals("2")) { //학생 수정
                                    editStudentInformation();
                                } else if (num.equals("3")) { //학생 삭제
                                    deleteStudentInformation();
                                } else { //숫자 잘못 입력
                                    System.out.println("올바르지 않은 번호입니다.");
                                }
                                break;
                            }
                            case "2": {
                                adminService.showTeacherList();
                                System.out.println("1.강사상세 2.강사수정 3. 강사삭제");
                                num = scanner.nextLine();

                                if (num.equals("1")) { //강사 상세
                                    showDetailTeacher();
                                } else if (num.equals("2")) { //강사 수정
                                    editTeacherInformation();
                                } else if (num.equals("3")) { //강사 삭제
                                    deleteTeacherInformation();
                                } else { //숫자 잘못 입력
                                }
                                break;
                            }
                            case "3": {
                                adminService.showLectureList();
                                System.out.println("1.강의상세 2.강의등록 3.강의삭제");
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
                                adminService.setAdmin((Admin) userService.logout());
                            }
                        }

                    }
                }
            }
            else if (num.equals("2")) {
                    signIn();
            }else{
                System.out.println("올바르지 않은 번호입니다.");
            }

        }while (true) ;

    }
    public void login(String choiceNum) throws IOException {

        //id input
        System.out.println("id 입력: ");
        String id = scanner.nextLine();
        //pw input
        System.out.println("pw 입력: ");

        String password = scanner.nextLine();
        if(choiceNum.equals("1")){
            studentService.setStudent(userService.loginStudent(id,password));
        }else if (choiceNum.equals("2")){
            teacherService.setTeacher(userService.loginTeacher(id,password));
        }else if(choiceNum.equals("3")){
            adminService.setAdmin(userService.loginAdmin(id,password));;
        }

    }
    public void signIn() throws IOException {
        System.out.println("1.학생 2.강사 번호를 입력해주세요");
        String choiceNum = scanner.nextLine();
        if(!(choiceNum.equals("1")||choiceNum.equals("2"))){
            System.out.println("올바르지 않은 번호 입니다.");
            return;
        }
        System.out.println("id: ");
        String id = scanner.nextLine();
        while (!ValidationSystem.isValidId(id) || userService.isExistStudent(id) || userService.isExistTeacher(id)) {
            //id 오류 출력
            System.out.println("다시 입력");
            id = scanner.nextLine();
        }
        System.out.println("pw: ");
        String password = scanner.nextLine();
        while (!ValidationSystem.isValidPassword(password)) {
            //pw 오류
            System.out.println("다시 입력");
            password = scanner.nextLine();
        }
        System.out.println("name: ");
        String name = scanner.nextLine();
        while (!ValidationSystem.isValidName(name)) {
            //name 오류 출력
            System.out.println("다시 입력");
            name = scanner.nextLine();
        }
        System.out.println("gender: ");
        String gender = scanner.nextLine();
        while (!ValidationSystem.isValidGender(gender)) {
            //gender 오류
            System.out.println("다시 입력");
            gender = scanner.nextLine();
        }
        System.out.println("phoneNumber: ");
        String phoneNumber = scanner.nextLine();
        while (!ValidationSystem.isValidPhoneNumber(phoneNumber)) {
            //phone 오류
            System.out.println("다시 입력");
            phoneNumber = scanner.nextLine();
        }
        System.out.println("birthday: ");
        String birthday = scanner.nextLine();
        while (!ValidationSystem.isValidBirthDay(birthday)) {
            //birth 오류 출력
            System.out.println("다시 입력");
            birthday = scanner.nextLine();
        }
        if(choiceNum.equals("1")) {
            System.out.println("accountNumber: ");
            String accountNumber = scanner.nextLine();
            while (!ValidationSystem.isValidaccountNumber(accountNumber)) {
                //accountNumber 오류
                System.out.println("다시 입력");
                accountNumber = scanner.nextLine();
            }
            System.out.println("accountpw: ");
            String accountPassword = scanner.nextLine();
            while (!ValidationSystem.isValidaccountPassword(accountPassword)) {
                //accountpw 오류
                System.out.println("다시 입력");
                accountPassword = scanner.nextLine();
            }


            userService.signInStudent(id, password, name, gender, phoneNumber, birthday, accountNumber, accountPassword);
        } else if (choiceNum.equals("2")) {
            System.out.println("email: ");
            String email = scanner.nextLine();
            while (!ValidationSystem.isValidEmail(email)) {
                //email 오류
                System.out.println("다시 입력");
                email = scanner.nextLine();
            }
            userService.signInTeacher(id,password,name,gender,phoneNumber,birthday,email);
        }


    }
    public void showDetailStudent() throws IOException {
        System.out.println("**********************************");
        String studentId;
        while(true) {
            System.out.println("상세정보를 확인하실 학생의 아이디를 입력해주세요: ");
            studentId = scanner.nextLine();
            if(adminService.detailStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    public void editStudentInformation() throws IOException {
        System.out.println("**********************************");
        String studentId;

        while(true) {
            System.out.println("수정하실 학생의 아이디를 입력해주세요: ");
            studentId = scanner.nextLine();
            if(adminService.showStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴

        // 메뉴 출력
        String value = "";
        int option = 0;
        while(true) {
            System.out.println("번호 입력 : ");
            option = Integer.parseInt(scanner.nextLine());

            if(0 < option && option < 5) break;
            else System.out.println("1 ~ 4 사이의 번호를 입력해주세요.");
        }

        switch(option) {
            case 1 :
                while(true) {
                    System.out.print("새로운 비밀번호 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.print("새로운 이름을 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.print("새로운 휴대폰번호를 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.print("새로운 계좌번호를 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidaccountPassword(value)){
                        System.out.println("계좌번호형식이 잘못 되셨습니다.");
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
            System.out.println("삭제하실 학생의 아이디를 입력해주세요: ");
            studentId = scanner.nextLine();
            if(adminService.newDeleteStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    //강사정보 상세보기
    public void showDetailTeacher() throws IOException {
        System.out.println("**********************************");
        String teacherId;

        while(true) {
            System.out.println("상세정보를 확인하실 강사의 아이디를 입력해주세요: ");
            teacherId = scanner.nextLine();
            if(adminService.detailTeacherInformation(teacherId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴
    }
    //강사정보수정
    public void editTeacherInformation() throws IOException {
        System.out.println("**********************************");
        String teacherId;

        while(true) {
            System.out.println("수정하실 강사의 아이디를 입력해주세요: ");
            teacherId = scanner.nextLine();
            if(adminService.showTeacherInformation(teacherId))break; // 수정할 학생 찾음
        } // 선생 찾아옴

        // 메뉴 출력

        String value = "";
        int option = 0;
        while(true) {
            System.out.println("번호 입력 : ");
            option = Integer.parseInt(scanner.nextLine());

            if(0 < option && option < 5) break;
            else System.out.println("1 ~ 4 사이의 번호를 입력해주세요.");
        }


        switch(option) {
            case 1 :
                while(true) {
                    System.out.print("새로운 비밀번호 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.print("새로운 이름을 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.print("새로운 휴대폰번호를 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.print("새로운 이메일을 입력해주세요: ");
                    value = scanner.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidEmail(value)){
                        System.out.println("이메일형식이 잘못 되셨습니다.");
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
            System.out.println("삭제하실 강사의 아이디를 입력해주세요: ");
            teacherId = scanner.nextLine();
            if(adminService.newDeleteTeacherInformation(teacherId))
                break;
        }
    }
    //강의정보 상세보기
    public void showDetailLecture() throws IOException {
        System.out.println("**********************************");
        String lectureId;

        while(true) {
            System.out.println("상세정보를 확인하실 강의 아이디를 입력해주세요: ");
            lectureId = scanner.nextLine();
            if(adminService.detailLectureInformation(lectureId))
                break;
        }
    }
    //강의등록
    public void registerLecture() throws IOException {
        System.out.println("**신규 강의 정보 등록**");

        System.out.print("[강의 이름]: ");
        String name = scanner.nextLine();

        System.out.println("[강의 요일]");
        System.out.println("[0]: 월요일 [1]: 화요일 [2]: 수요일 [3]: 목요일 [4]: 금요일");
        System.out.print("입력: ");
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
        System.out.print("입력: ");
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

        while (!isTeacherExist) {
            System.out.print("[담당 강사 아이디]: ");
            teacherId = scanner.nextLine();
            isTeacherExist = adminService.isExistTeacher(teacherId);
            //lectureRepository있으면,
            // teacherFound = lectureRepository.isExist(teacherId);

            if (isTeacherExist) {
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
            System.out.println("삭제하실 강의 아이디를 입력해주세요: ");
            lectureId = scanner.nextLine();
            if(adminService.newDeleteLectureInformation(lectureId))
                break;
        }
    }


}
