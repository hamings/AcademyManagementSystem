package src.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import src.ServiceType;
import src.domain.Lecture;
import src.domain.LectureRegistration;
import src.domain.Notification;
import src.domain.Student;
import src.domain.StudyRoom;
import src.domain.Teacher;
import src.repository.Repository;
import src.repository.RepositoryProvider;

public class StudentService {
    // 강의 repo 불러오기
    private Repository<Lecture, String> lectureRepository;
    // 수강신청 repo 불러오기
    private Repository<LectureRegistration, String> lectureRegistrationRepository;
    // 알림 repo 불러오기
    private Repository<Notification, Integer> notificationRepository;
    private Repository<Student, String> studentRepository;



    public StudentService() throws IOException {
        this.lectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.lectureRegistrationRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.notificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    Student student = new Student();
    StudyRoom studyRoom = new StudyRoom();

    boolean[][] checkSeat = studyRoom.getCheckSeat();
    // 굳이?????
    Map<String, String> reservationMap = studyRoom.getReservationMap();

    Scanner sc = new Scanner(System.in);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 0. 학생으로 로그인 후 첫 페이지인 메뉴 보이기
    public void showStudentMenu() {
        System.out.println("------------------");
        System.out.println("-----학생 관리-----");
        System.out.println("------------------");
        System.out.println("1. 수강 관리");
        System.out.println("2. 자습실 예약");
        System.out.println("3. 납부 알림 확인");
    }

    // 옵션 고르기
    public int myOption() {
        // 세 가지 옵션 중 하나 선택 (각 함수로 이동)
        System.out.print("원하시는 서비스의 번호를 입력하세요 : ");
        int option = Integer.parseInt(sc.nextLine());
        return option;
    }

    // 1. 수강 관리

    // 전체 강의 리스트 출력
    public void showAllLectureList() throws IOException {
        // lectureRepository 에서 findAll() 메서드 호출
        List<Lecture> allLectures = lectureRepository.findAll();
        // 가져온 강의 목록 처리
        for(Lecture lecture : allLectures) {
            System.out.print("강의 ID: " + lecture.getLectureId() + "\t");
            System.out.print("강의 이름: " + lecture.getLectureName() + "\t");
            System.out.print("강의 요일: " + lecture.getLectureTime() + "\t");
            System.out.print("강의 시간: " + lecture.getLectureDay() + "\t");
            System.out.println("강사: " + lecture.getLectureTeacherId() + "\t");
            // 강사 이름이 나와야 하는거 아닌가.....?
            // Teacher teacher = teacherR

            //Teacher teacher = teacherRepository.findById(lecture.getTeacherId());

            System.out.println(); // 강의 사이에 공백 라인 추가
        }
    }


}
