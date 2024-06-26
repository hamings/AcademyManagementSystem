package src.service;

import src.ServiceType;
import src.domain.*;
import src.repository.Repository;
import src.repository.RepositoryProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AdminService {


    private Repository<Student, String> studentRepository;
    private Repository<Teacher, String> teacherRepository;
    private Repository<Lecture, String> lectureRepository;
    private Repository<Notification, String> notificationRepository;
    private Bank bank;
    private Admin admin;
    private LocalDate lastPaymentDay;


    public AdminService() throws IOException {
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        this.teacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        this.lectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.notificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);
        this.bank = new Bank();
        this.lastPaymentDay = LocalDate.of(2024, 2, 26);
    }

    public Admin getAdmin() {
        return this.admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public boolean checkInAmsAccount(String accountNumber) {
        return bank.checkAmsAccount(accountNumber);
    }

    //학원결제시스템
    public void doPaymentSystem() throws IOException {
        LocalDate nowDate = LocalDate.now();
        if (lastPaymentDay != null && Period.between(lastPaymentDay, nowDate).getMonths() != 1) {
            System.out.println("[이번달 결제는 이미 완료되었습니다!]");
            System.out.println("[다음 결제일]: " + lastPaymentDay.plusMonths(1));
            return;
        }
        lastPaymentDay = nowDate;

        List<Student> studentList = studentRepository.findAll();
        List<String> successList = new ArrayList<>();
        List<String> failList = new ArrayList<>();
        for (Student student : studentList) {
            boolean checkAccount = bank.checkAccount(student.getAccountNumber(), student.getAccountPassword());
            boolean paymentAccount = bank.accountTransfer(student.getAccountNumber(), student.getLectureCost());
            System.out.println(student.getLectureCost());
            Long result = bank.getBalance(student.getAccountNumber());//결제가 완료되고 남은 학생 잔액

            String adminContent = "";
            String studentContent = "";

            if (checkAccount) {
                if (paymentAccount) {
                    adminContent = "납부완료";
                    studentContent = "회원님의 계좌에서 정상적으로 출금을 완료하였습니다.";
                    successList.add(student.getName());
                } else {
                    adminContent = "미납대상1";
                    studentContent = "계좌의 잔액부족으로 출금이 실패하였습니다.";
                    failList.add(student.getName());
                }
            } else {
                adminContent = "미납대상2";
                studentContent = "계좌정보미일치로 인해 출금이 실패하였습니다.";
                failList.add(student.getName());
            }

            Notification notification = new Notification(1, 0, adminContent, studentContent, student.getId(), LocalDateTime.now(), result);
            Queue<Notification> notificationQ = (Queue<Notification>) notificationRepository.findById(student.getId());

            if (notificationQ.size() >= 10) {
                notificationQ.poll();
            }
            notificationQ.add(notification);

            notificationRepository.save();
        }
        //관리자서비스 - 한달 학원비 정산
        System.out.println("        [학원비 납부인원 현황]         ");
        System.out.println("----------------------------------");
        System.out.println("학원비 납부 완료: " + successList.size() + "명");
        System.out.println("학원비 미납: " + failList.size() + "명");
        System.out.println("----------------------------------");
        System.out.println("           [학원비 미납자]");
        for (String name : failList) {
            System.out.println("*********************************");
            System.out.println("             " + name + "          ");
            System.out.println("*********************************");
        }
    }

    //수정할 수 있는 수정메뉴 출력
    public boolean showStudentInformation(String studentId) throws IOException {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("입력 아이디에 해당하는 학생정보가 없습니다.");
            return false;
        }
        student.printEditStudentInformation();
        return true;
    }

    //수정할 수 있는 수정메뉴 출력
    public boolean showTeacherInformation(String teacherId) throws IOException {
        Teacher teacher = teacherRepository.findById(teacherId);
        if (teacher == null) {
            System.out.println("입력 아이디에 해당하는 강사정보가 없습니다.");
            return false;
        }
        teacher.printEditTeacherInformation();
        return true;
    }

    //첫번째화면 - 학생정보출력(기능은 아님) - 이름, 아이디만
    public void showStudentList() throws IOException {
        List<Student> StudentList = studentRepository.findAll();

        if (StudentList.isEmpty()) {
            System.out.println("    [현재 등록중인 학생이 없습니다.]");
        }
        System.out.println("            [학생리스트]           ");
        System.out.println("**********************************");
        for (Student student : StudentList) {
            student.printStudentInformation();
        }
    }

    //1. (입력받은 아이디의 학생)상세정보출력
    public boolean detailStudentInformation(String studentId) throws IOException {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("입력 아이디에 해당하는 학생정보가 없습니다.");
            return false;
        }
        student.printDetailStudentInformation();
        return true;
    }

    //2. 학생정보 수정
    public void editStudentInformation(String studentId, int option, String value) throws IOException {
        Student student = studentRepository.findById(studentId);//수정할 학생 찾음

        switch (option) {
            case 1:
                student.setPassword(value);
                System.out.println("-------------------------------");
                System.out.println("학생의 비밀번호 수정이 완료되었습니다!");
                System.out.println("수정된 학생의 비밀번호: " + value);
                studentRepository.save();
                break;
            case 2:
                student.setName(value);
                System.out.println("----------------------------");
                System.out.println("학생의 이름이 수정 완료되었습니다!");
                System.out.println("수정된 학생의 이름: " + value);
                studentRepository.save();
                break;
            case 3:
                student.setPhoneNumber(value);
                System.out.println("--------------------------------");
                System.out.println("학생의 휴대폰번호가 수정 완료되었습니다!");
                System.out.println("수정된 학생의 휴대폰번호: " + value);
                studentRepository.save();
                break;
            case 4:
                student.setAccountPassword(value);
                System.out.println("----------------------------------");
                System.out.println("학생의 계좌비밀번호가 수정 완료되었습니다!");
                System.out.println("수정된 학생의 계좌비밀번호: " + value);
                studentRepository.save();
                break;
        }
    }

    //학생정보 삭제
    public boolean deleteStudentInformation(String studentId) throws IOException {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("입력 아이디에 해당하는 학생정보가 없습니다!");
            System.out.println();
            return false;
        }
        studentRepository.delete(student);
        System.out.println("[입력하신 학생의정보가 삭제되었습니다!]");
        System.out.println();
        studentRepository.save();
        return true;
    }

    //첫번째화면 - 강사정보출력(기능은 아님) - 이름, 아이디만
    public void showTeacherList() throws IOException {
        List<Teacher> TeacherList = teacherRepository.findAll();
        System.out.println("           [강사리스트]         ");
        System.out.println("**********************************");
        for (Teacher teacher : TeacherList) {
            teacher.printTeacherInformation();
        }
    }

    //1. (입력받은 아이디의 강사)상세정보출력
    public boolean detailTeacherInformation(String teacherId) throws IOException {
        Teacher teacher = teacherRepository.findById(teacherId);
        if (teacher == null) {
            System.out.println("입력 아이디에 해당하는 강사정보가 없습니다.");
            return false;
        }
        teacher.printDetailTeacherInformation();
        return true;
    }

    //2. 강사정보 수정
    public void editTeacherInformation(String teacherId, int option, String value) throws IOException {
        Teacher teacher = teacherRepository.findById(teacherId);//수정할 학생 찾음

        switch (option) {
            case 1:
                teacher.setPassword(value);
                System.out.println("강사의 비밀번호 수정이 완료되었습니다.");
                System.out.println("수정된 강사의 비밀번호: " + value);
                teacherRepository.save();
                break;
            case 2:
                teacher.setName(value);
                System.out.println("강사의 이름이 수정 완료되었습니다.");
                System.out.println("수정된 강사의 이름: " + value);
                teacherRepository.save();
                break;
            case 3:
                teacher.setPhoneNumber(value);
                System.out.println("강사의 휴대폰번호가 수정 완료되었습니다.");
                System.out.println("수정된 강사의 휴대폰번호: " + value);
                teacherRepository.save();
                break;
            case 4:
                teacher.setEmail(value);
                System.out.println("강사의 이메일이 수정 완료되었습니다.");
                System.out.println("수정된 강사의 이메일: " + value);
                teacherRepository.save();
                break;
        }
    }

    //강사정보 삭제
    public boolean deleteTeacherInformation(String teacherId) throws IOException {
        Teacher teacher = teacherRepository.findById(teacherId);
        if (teacher == null) {
            System.out.println("입력 아이디에 해당하는 강사정보가 없습니다.");
            return false;
        }
        teacherRepository.delete(teacher);
        System.out.println("[입력하신 강사의정보가 삭제되었습니다!]");
        System.out.println();
        teacherRepository.save();
        return true;
    }

    //첫번째화면 - 강의정보출력(기능은 아님)
    public void showLectureList() throws IOException {
        List<Lecture> LectureList = lectureRepository.findAll();
        System.out.println("                [강의리스트]         ");
        System.out.println("*********************************************");

        if (LectureList.isEmpty()) {
            System.out.println("         [현재 등록된 강의는 없습니다.]");
            return;
        }

        for (Lecture lecture : LectureList) {
            lecture.printLectureInformation();
        }
    }

    //1. 강의상세정보 출력
    public void showDetailLectureList() throws IOException {
        List<Lecture> LectureList = lectureRepository.findAll();

        for (Lecture lecture : LectureList) {
            lecture.printDetailLectureInformation();
        }
    }

    //1. (입력받은 아이디의 학생)상세정보출력
    public boolean detailLectureInformation(String lectureId) throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId);
        if (lecture == null) {
            System.out.println("입력 아이디에 해당하는 강의정보가 없습니다.");
            return false;
        }
        lecture.printDetailLectureInformation();
        return true;
    }


    //2.새로운 강의 등록
    public void registerLecture(String name, int day, int time, String teacherName, String teacherId) throws IOException {
        int id = lectureRepository.findAll().size() + 1; // 코드 수정 필요

        Lecture lecture = new Lecture(name + id, name, day, time, teacherName, teacherId);

        Teacher teacher = teacherRepository.findById(teacherId);
        List<Lecture> lectureList = teacher.getLectureList();
        List<String> lectureIdList = teacher.getLectureIdList();

        lectureList.add(lecture);
        lectureIdList.add(lecture.getLectureId());

        lectureRepository.insert(lecture);
        lectureRepository.save();
        teacherRepository.save();
    }

    //해당아이디의 강의가 존재하는지
    public boolean isExistTeacher(String teacherId) {
        return teacherRepository.isExist(teacherId);
    }

    // 동일한 요일, 시간의 강의가 존재하는지
    public boolean isExistSameTimeLecture(String teacherId, int day, int time) throws IOException {
        Teacher teacher = teacherRepository.findById(teacherId);

        List<Lecture> lectureList = teacher.getLectureList();
        if (lectureList.isEmpty()) {
            return false;
        }
        for (Lecture lecture : lectureList) {
            if (lecture.getLectureDay() == day && lecture.getLectureTime() == time) {
                return true;
            }
        }
        return false;
    }

    //강사 아이디로 강사 이름반환
    public String getTeacherName(String teacherId) throws IOException {

        return teacherRepository.findById(teacherId).getName();
    }

    //강의정보 삭제
    public boolean deleteLectureInformation(String lectureId) throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId);
        if (lecture == null) {
            System.out.println("입력 아이디에 해당하는 강의정보가 없습니다.");
            return false;
        }
        lectureRepository.delete(lecture);
        System.out.println("[입력하신 강의정보가 삭제되었습니다!]");
        System.out.println();
        lectureRepository.save();
        return true;
    }
}
