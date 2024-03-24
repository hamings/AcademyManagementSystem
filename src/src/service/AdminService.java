package src.service;

import src.ServiceType;
import src.domain.*;
import src.repository.Repository;
import src.repository.RepositoryProvider;
import src.util.ValidationSystem;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminService {



    private Repository<Student,String> StudentRepository;
    private Repository<Teacher,String> TeacherRepository;
    private Repository<Lecture,String> LectureRepository;
    private Repository<Notification,String> NotificationRepository;
    private Bank bank = new Bank();

    public AdminService() throws IOException {
        this.StudentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        this.TeacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        this.LectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.NotificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);
    }

    //학원결제시스템
    public void bankSystem() throws IOException {
        List<Student> studentList = StudentRepository.findAll();
        for (Student student : studentList) {
            boolean checkAccount = bank.checkAccount(student.getAccountNumber(), student.getAccountPassword());
            boolean paymentAccount = bank.paymentAccount(student.getAccountNumber(), student.getLectureCost());
            Long result = bank.finalBalance(student.getAccountNumber());//결제가 완료되고 남은 학생 잔액
            if(checkAccount){
                if(paymentAccount){
                    NotificationRepository.insert(new Notification(1,0,"학생출금완료","출금완료",student.getId(),LocalDateTime.now(),result));
                } else{
                    NotificationRepository.insert(new Notification(2,0,"학생출금실패(잔액실패)","출금실패(계좌정보조회실패)",student.getId(),LocalDateTime.now(),result));
                }
            }else{
                NotificationRepository.insert(new Notification(2,0,"학생출금실패(계좌정보조회실패)","출금실패(계좌정보조회실패)",student.getId(),LocalDateTime.now(),result));
            }

            NotificationRepository.save();
        }
    }

    //수정할 수 있는 수정메뉴 출력
    public boolean showStudentInformation(String studentId) throws IOException {
        Student student = StudentRepository.findById(studentId);
        if(student == null) {
            System.out.println("입력 id에 해당하는 학생정보가 없습니다.");
            return false;
        }
        // 학생정보 출력 메서드
        // 출력 정의 -> student tostring 호출
        student.editStudentInformation();
        return true;
    }

    //수정할 수 있는 수정메뉴 출력
    public boolean showTeacherInformation(String teacherId) throws IOException {
        Teacher teacher = TeacherRepository.findById(teacherId);
        if(teacher == null) {
            System.out.println("입력 id에 해당하는 강사정보가 없습니다.");
            return false;
            }
        // 강사정보 출력 메서드
        // 출력 정의 -> teacher tostring 호출
        teacher.editTeacherInformation();
        return true;
    }

    //첫번째화면 - 학생정보출력(기능은 아님) - 이름, 아이디만
    public void showStudentList() throws IOException {
        List<Student> StudentList = StudentRepository.findAll();

        for (Student student : StudentList) {
            student.printStudentInformation();
        }
    }

    //1. 학생상세정보출력
    public void showDetailStudentList() throws IOException {
        List<Student> StudentList = StudentRepository.findAll();

        for (Student student : StudentList) {
            student.printDetailStudentInformation();
        }
    }

    //2. 새로운 학생정보 수정함수
    public void newEditStudentInformation(String studentId, int option, String value) throws IOException {
        Student student = StudentRepository.findById(studentId);//수정할 학생 찾음

        switch (option) {
            case 1:
                student.setPassword(value);
                System.out.println("학생의 비밀번호 수정이 완료되었습니다.");
                System.out.println("수정된 학생의 비밀번호: " + value);
                break;
            case 2:
                student.setName(value);
                System.out.println("학생의 이름이 수정 완료되었습니다.");
                System.out.println("수정된 학생의 이름: " + value);
                break;
            case 3:
                student.setPhoneNumber(value);
                System.out.println("학생의 휴대폰번호가 수정 완료되었습니다.");
                System.out.println("수정된 학생의 휴대폰번호: " + value);
                break;
            case 4:
                student.setAccountPassword(value);
                System.out.println("학생의 계좌비밀번호가 수정 완료되었습니다.");
                System.out.println("수정된 학생의 계좌비밀번호: " + value);
                break;
        }
    }

    //학생정보삭제
    public void deleteStudentInformation() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("삭제하실 학생의 아이디를 입력해주세요: ");

        String StudentId = sc.nextLine();//삭제할 학생의 아이디 입력
        Student targetDeleteStudent = StudentRepository.findById(StudentId);
        StudentRepository.delete(targetDeleteStudent);
        System.out.println("입력하신 학생의정보가 삭제되었습니다.");

        StudentRepository.save();
    }

    //첫번째화면 - 강사정보출력(기능은 아님) - 이름, 아이디만
    public void showTeacherList() throws IOException {
        List<Teacher> TeacherList = TeacherRepository.findAll();

        for (Teacher teacher : TeacherList) {
            teacher.printTeacherInformation();
        }
    }

    //1. 강사상세정보출력
    public void showDetailTeacherList() throws IOException {
        List<Teacher> TeacherList = TeacherRepository.findAll();

        for (Teacher teacher : TeacherList) {
            teacher.printDetailTeacherInformation();
        }
    }

    //새로운 강사정보 수정
    public void newEditTeacherInformation(String teacherId, int option, String value) throws IOException {
        Teacher teacher = TeacherRepository.findById(teacherId);//수정할 학생 찾음

        switch (option) {
            case 1:
                teacher.setPassword(value);
                System.out.println("강사의 비밀번호 수정이 완료되었습니다.");
                System.out.println("수정된 강사의 비밀번호: " + value);
                break;
            case 2:
                teacher.setName(value);
                System.out.println("강사의 이름이 수정 완료되었습니다.");
                System.out.println("수정된 강사의 이름: " + value);
                break;
            case 3:
                teacher.setPhoneNumber(value);
                System.out.println("강사의 휴대폰번호가 수정 완료되었습니다.");
                System.out.println("수정된 강사의 휴대폰번호: " + value);
                break;
            case 4:
                teacher.setEmail(value);
                System.out.println("강사의 이메일이 수정 완료되었습니다.");
                System.out.println("수정된 강사의 이메일: " + value);
                break;
        }
    }

    //첫번째화면 - 강의정보출력(기능은 아님)
    public void showLectureList() throws IOException {
        List<Lecture> LectureList = LectureRepository.findAll();

        for (Lecture lecture : LectureList) {
            lecture.printLectureInformation();
        }
    }

    //1. 강의상세정보출력
    public void showDetailLectureList() throws IOException {
        List<Lecture> LectureList = LectureRepository.findAll();

        for (Lecture lecture : LectureList) {
            lecture.printDetailLectureInformation();
        }
    }

    //2. 신규강의정보등록
    public void registerLecture() throws IOException {
        List<Teacher> teacherList = TeacherRepository.findAll(); // 변수명은 소문자로 시작하는 것이 자바의 관례입니다.
        Scanner sc = new Scanner(System.in);
        int count  = LectureRepository.findAll().size()+1;

        System.out.println("**신규 강의 정보 등록**");

        System.out.print("[강의 이름]: ");
        String name = sc.nextLine();
        String id = name + count;//강의 아이디 자동생성(강의이름 + 카운팅수)

        System.out.println("[강의 요일]");
        System.out.print("[0]: 월요일 [1]: 화요일 [2]: 수요일 [3]: 목요일 [4]: 금요일");
        int day = -1;
        boolean dayCheck = false;
        while(!dayCheck){
            int intputDay = Integer.parseInt(sc.nextLine());
            if(0<=intputDay && intputDay<5){
                day = intputDay;
                dayCheck = true;
                break;
            } else{
                System.out.print("올바른 번호를 입력해주세요: ");
            }
        }

        System.out.println("[강의 시간]");
        System.out.print("[0] 10:00 ~ 12:00 [1] 13:00 ~ 14:50 [2] 15:00 ~ 16:50 [3] 17:00 ~ 19:00 ");
        int time = -1;
        boolean timeCheck = false;
        while(!timeCheck){
            int inputTime = Integer.parseInt(sc.nextLine());
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
        boolean teacherFound = false;

        while (!teacherFound) {
            System.out.print("[담당 강사 아이디]: ");
            String inputTeacherId = sc.nextLine();
            for (Teacher teacher : teacherList) {
                if(teacher.getId().equals(inputTeacherId)) {
                    teacherId = inputTeacherId;
                    teacherName = teacher.getName();
                    teacherFound = true; // 올바른 선생님을 찾았으므로 반복을 종료합니다.
                    break; // for 루프를 빠져나옵니다.
                }
            }

            if (teacherFound) {
                Lecture newLecture = new Lecture(id, name, day, time, teacherName, teacherId);
                LectureRepository.insert(newLecture);
                LectureRepository.save();
                System.out.println("강의가 성공적으로 등록되었습니다.");
                count++;
            } else {
                System.out.println("해당 아이디의 강사님은 존재하지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    //3. 강의삭제
    public void deleteLectureInformation() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("삭제하실 강의 아이디를 입력해주세요: ");

        String LectureId = sc.nextLine();//삭제할 강의 아이디 입력
        Lecture targetDeleteLecture = LectureRepository.findById(LectureId);
        LectureRepository.delete(targetDeleteLecture);
        System.out.println("입력하신 강의정보가 삭제되었습니다.");
        LectureRepository.save();
    }
}
