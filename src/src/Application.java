package src;

import src.domain.*;
import src.repository.NotificationRepository;
import src.repository.Repository;
import src.repository.RepositoryProvider;
import src.service.AdminService;
import src.util.FileSystem;
import src.util.ValidationSystem;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.*;

public class Application {
    static AdminService adminService;

    static {
        try {
            adminService = new AdminService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        //initTestSet();
        adminService.showDetailLectureList();


//        Map<Integer, Notification> notificationMap = FileSystem.loadObjectMap(ServiceType.NOTIFICATION);
//
//        for(Integer key : notificationMap.keySet()) {
//            System.out.println(notificationMap.get(key));
//        }

//
        //student.setLectureCost(100000);
        /*
        1. 수강신청 예시

         */


    }


    public static void initTestSet() throws IOException {
        Map<String, Student> studentMap = new HashMap<>();

        Student student = new Student("koo","qwe123","1999","구태호","1234","1111","01000000000","23" +
                5000L);
        student.setId("koo");
        student.setGender("Male");
        student.setBirthday("1999");
        student.setName("구태호");
        student.setAccountNumber("1234");
        student.setAccountPassword("1111");
        student.setPhoneNumber("01000000000");
        student.setLectureRegistrationIdList(new ArrayList<>());
        student.getLectureRegistrationIdList().add(1L);

        Student student2 = new Student("koo","Male","1999","구태호","1234","1111","01000000000","23" +
                5000L);
        student2.setId("kim");
        student2.setGender("Male");
        student2.setBirthday("1999");
        student2.setName("김민수");
        student2.setAccountNumber("5678");
        student2.setAccountPassword("1111");
        student2.setPhoneNumber("01022222222");
        student2.setLectureRegistrationIdList(new ArrayList<>());
        student2.getLectureRegistrationIdList().add(1L);

        studentMap.put(student.getId(), student);
        studentMap.put(student2.getId(), student2);
        FileSystem.saveObjectMap(ServiceType.STUDENT, studentMap);

        Map<String, Teacher> teacherMap = new HashMap<>();
        Teacher teacher = new Teacher();
        teacher.setLectureIdList(new ArrayList<>());
        teacher.getLectureIdList().add("java001");
        teacher.setId("lim");
        teacher.setGender("Male");
        teacher.setEmail("kosa@naver.com");
        teacher.setPhoneNumber("01000000000");
        teacher.setPassword("1234aab");
        teacher.setBirthday("1980");
        teacher.setName("임경균");

        teacherMap.put(teacher.getId(), teacher);
        FileSystem.saveObjectMap(ServiceType.TEACHER, teacherMap);

        Map<String, Lecture> lectureMap = new HashMap<>();
        Lecture lecture = new Lecture();
        lecture.setLectureId("java001");
        lecture.setLectureDay(0);
        lecture.setLectureName("이것이자바다");
        lecture.setLectureTime(0);
        lecture.setLectureTeacherName("임경균");
        lecture.setLectureTeacherId("lim");
        lecture.setLectureRegistrationIdList(new ArrayList<>());
        lecture.getLectureRegistrationIdList().add(1L);

        lectureMap.put(lecture.getLectureId(), lecture);
        FileSystem.saveObjectMap(ServiceType.LECTURE, lectureMap);


        Map<Long, LectureRegistration> lectureRegistrationMap = new HashMap<>();
        LectureRegistration l = new LectureRegistration();
        l.setId(1L);
        l.setLectureDay("화요일");
        l.setLectureId("java001");
        l.setStudentId("koo");
        l.setLectureTime(0);

        lectureRegistrationMap.put(l.getId(), l);
        FileSystem.saveObjectMap(ServiceType.LECTUREREGISTRATION, lectureRegistrationMap);

        Map<Integer, Notification> notificationMap = new HashMap<>();
        notificationMap.put(0, new Notification(0,0,"","","", LocalDateTime.now(),1000L));
        FileSystem.saveObjectMap(ServiceType.NOTIFICATION, notificationMap);
    }
    public static  void test() throws IOException {
        // [STEP 1]필요 레포지토리 받아오기
        RepositoryProvider rp = RepositoryProvider.getInstance();
        Repository<Student, String> repository = rp.provide(ServiceType.STUDENT);

        // [OPTION1] id로 객체 검색 -> 반환받은 객체 데이터 (update) 조작
        Student s = repository.findById("koo");
        System.out.println(s.getLectureRegistrationList().get(0).getLectureId());
        // student 데이터 set + save() : save() 사용은 File에 반영하겠다.
        s.setName("구태호님");
        repository.save();
        //업데이트 된 내용 확인
        s = repository.findById("koo");
        System.out.println(s.getName()); // 업데이트 됐으면 구태호님 이라는 문구 출력 될 것


        // [OPTION2] 새로운 객체 생성 -> 레포지토리에 추가
        //Student newStudent = new Student();
        //newStudent.setName("김우재");
        // .. 다른 속성들 모두 set 진행 후 레포지토리에 insert
        //repository.insert(newStudent);
        // save()를 통해 파일에 반영
        repository.save();


        // [OPTION3] 객체 삭제
        // 객체 삭제 로직 -> 레포지토리로부터 받아온 객체([주의]직접 new 한 객체 삭제하는 거 아님)-> delete메서드에 파라미터로 넘김 -> save()


    }
    public static void editStudentInformation() throws IOException {
        System.out.println("**********************************");
        Scanner sc = new Scanner(System.in);
        String studentId;

        while(true) {
            System.out.println("수정하실 학생의 아이디를 입력해주세요: ");
            studentId = sc.nextLine();
            if(adminService.showStudentInformation(studentId))
                break; // 수정할 학생 찾음
        } // 학생 찾아옴

        // 메뉴 출력
        String value = "";
        int option = 0;
        while(true) {
            System.out.println("번호 입력 : ");
            option = Integer.parseInt(sc.nextLine());

            if(0 < option && option < 5) break;
            else System.out.println("1 ~ 4 사이의 번호를 입력해주세요.");
        }

        switch(option) {
            case 1 :
                while(true) {
                    System.out.print("새로운 비밀번호 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.print("새로운 이름을 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.print("새로운 휴대폰번호를 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.print("새로운 계좌번호를 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidaccountPassword(value)){
                        System.out.println("계좌번호형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
        }

        adminService.newEditStudentInformation(studentId, option, value);
    }


    public static void editTeacherInformation() throws IOException {
        System.out.println("**********************************");
        Scanner sc = new Scanner(System.in);
        String teacherId;

        while(true) {
            System.out.println("수정하실 강사의 아이디를 입력해주세요: ");
            teacherId = sc.nextLine();
            if(adminService.showTeacherInformation(teacherId))break; // 수정할 학생 찾음
        } // 선생 찾아옴

        // 메뉴 출력

        String value = "";
        int option = 0;
        while(true) {
            System.out.println("번호 입력 : ");
            option = Integer.parseInt(sc.nextLine());

            if(0 < option && option < 5) break;
            else System.out.println("1 ~ 4 사이의 번호를 입력해주세요.");
        }


        switch(option) {
            case 1 :
                while(true) {
                    System.out.print("새로운 비밀번호 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPassword(value)){
                        System.out.println("비밀번호형식 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 2 :
                while(true) {
                    System.out.print("새로운 이름을 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidName(value)){
                        System.out.println("이름형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 3:
                while(true) {
                    System.out.print("새로운 휴대폰번호를 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidPhoneNumber(value)){
                        System.out.println("휴대폰번호형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
            case 4:
                while(true) {
                    System.out.print("새로운 이메일을 입력해주세요: ");
                    value = sc.nextLine();
                    //휴대폰번호, 형식 맞나 검사
                    if(!ValidationSystem.isValidEmail(value)){
                        System.out.println("이메일형식이 잘못 되셨습니다.");
                    } else break;
                }
                break;
        }

        adminService.newEditTeacherInformation(teacherId, option, value);
    }



}