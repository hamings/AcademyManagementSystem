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

    // 수강 신청 내역 가져와 출력
    public void showStudentAllRegistrationLecture(String studentId) throws IOException {
        // 수강 신청 내역 가져오기
        // 필터링 (해당 학생의 수강 신청 내역)
        List<LectureRegistration> studentLectureRegistration = lectureRegistrationRepository.findAll().stream()
                .filter(registration -> registration.getStudentId().equals(studentId))
                .collect(Collectors.toList());

        // 해당 학생 수강 신청 내역 출력
        if(studentLectureRegistration.isEmpty()) {
            // 만약 수강 신청 내역이 없다면
            System.out.println("수강 신청 내역이 없습니다.");
        } else {
            System.out.println("수강 신청 내역");
            for (LectureRegistration registration : studentLectureRegistration) {
                System.out.println(registration);
                //
            }
        }

    }

    // 수강 신청
    public void registerLecture() throws IOException {
        // 원하는 강의 고르기
        System.out.print("수강하고자 하는 강의의 강의 id를 입력해주세요: ");
        String choiceLectureId = sc.nextLine();

        // 강의 리스트에서 해당 강의 정보 가져오기
        Lecture pickLecture = lectureRepository.findById(choiceLectureId);
        if (pickLecture == null) {
            System.out.println("존재하지 않는 강의입니다.");
            return;
        }
        // 학생 수강 신청 내역 (GPT 코드)
        Student student = studentRepository.findById("koo");


        List<LectureRegistration> studentRegistrations = student.getLectureRegistrationList();
        //lectureRegistrationRepository.findAllByStudentId(student.getId());
        // 이 코드의 문제 : lectureRegistrationRepository 에 findAllByStudentId() 가 없다는 것

        //new Enrollment("koo", choiceLectureId);

        // 이 강의의 시간 가져오기
        // String lectureTime = pickLecture.getLectureTime();
        // 그냥 떠오르는 코드 쓴거라.... 맞는지 모르겠는데.......

        // 내 시간표에서 해당 시간에 수강 신청한 강의가 있는지 확인하기 > 없으면 수강 등록, 없으면 수강 실패 문구
//        boolean alreadyRegistered = studentRegistrations.stream()
//                .anyMatch(registration -> registration.getLectureDay().equals(lectureTime));


    }

    // 수강 취소
    public void deleteLecture() {
        // 내 시간표 보여주기


        // 취소하고자 하는 강의 고르기
        System.out.print("취소하고자 하는 강의의 강의 id를 입력해주세요: ");
        String choiceLectureId = sc.nextLine();

        // 내 시간표에 해당 id 강의가 있는지 확인 > 있으면 삭제, 없으면 취소 실패 문구

    }

    // 2. 자습실 예약
    // 해당 서비스 메뉴 보이기
    public void showStudyRoomMenu() {
        System.out.println("------------------");
        System.out.println("-----자습실 예약-----");
        System.out.println("------------------");
        System.out.println("1. 예약하기");
        System.out.println("2. 예약취소하기");
    }

    // 자습실 좌석표 출력
    public void showStudyRoomStatus(StudyRoom studyRoom) {
        //boolean[][] checkSeat = studyRoom.getCheckSeat();

        for (int i = 0; i < checkSeat.length; i++) {
            for (int j = 0; j < checkSeat[i].length; j++) {
                //System.out.print(checkSeat[i][j] ? "O " : "X "); // 예약된 좌석은 "O", 비어있는 좌석은 "X"로 출력
                System.out.print(checkSeat[i][j] ? reservationMap.get(String.valueOf(i+"-"+j)) : System.out.printf("[%d-%d] ",i+1,j+1));
                // 만약 예약된 좌석이면 map 에서 해당 좌석 번호로 타고 들어가 key 값인 studentId 가져오기
                // String.valueOf(i+"-"+j)) 이거 아님!!!!
                // value 를 가지고 key 뽑아낼 수 있나?
                // 만약 빈 좌석이면 1-1, 2-1 등 좌석 번호 보이기
            }
            System.out.println(); // 다음 줄로 넘어감
        }
        System.out.println("*****************************************");
    }

}
