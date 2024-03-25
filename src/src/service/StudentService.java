package src.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
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
    private Repository<Teacher, String> teacherRepository;
    private StudyRoom studyRoom;
    Student student;
    Lecture lecture;



    public StudentService() throws IOException {
        this.lectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.lectureRegistrationRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTUREREGISTRATION);
        this.notificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        this.teacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        this.studyRoom = new StudyRoom();
        student = studentRepository.findById("koo");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        return Integer.parseInt(sc.nextLine());
    }

    // 1. 수강 관리

    // 전체 강의 리스트 출력
    public void showAllLectureList() throws IOException {
        // lectureRepository 에서 findAll() 메서드 호출
        List<Lecture> allLectures = lectureRepository.findAll();
        // 가져온 강의 목록 처리
        for (Lecture lecture : allLectures) {
            System.out.print("강의 ID: " + lecture.getLectureId() + "\t");
            System.out.print("강의 이름: " + lecture.getLectureName() + "\t");
            System.out.print("강의 요일: " + lecture.getLectureTime() + "\t");
            System.out.print("강의 시간: " + lecture.getLectureDay() + "\t");
            System.out.println("강사: " + lecture.getLectureTeacherName() + "\t");
            //System.out.println(); // 강의 사이에 공백 라인 추가
        }
    }

    // 수강 신청 내역 가져와 출력
    public void showStudentAllRegistrationLecture(String studentId) throws IOException {
        //수강 신청 내역 가져오기
        //필터링 (해당 학생의 수강 신청 내역)
        List<LectureRegistration> studentLectureRegistration = lectureRegistrationRepository.findAll().stream()
                .filter(registration -> registration.getStudentId().equals(studentId))
                .collect(Collectors.toList());

        // 해당 학생 수강 신청 내역 출력
        System.out.println("수강 신청 내역");
        System.out.println(studentLectureRegistration);

    }

    // 강의 요일 (int -> str) 바꿔주는 함수
    // lectureDay를 숫자로 받으면 각 번호에 맞는 요일로 리턴
    String intLectureDayToRealDay (int lectureDay) {
        if(lectureDay == 0){
            return "월요일";
        } else if(lectureDay == 1){
            return "화요일";
        }  else if(lectureDay == 2){
            return "수요일";
        }  else if(lectureDay == 3){
            return "목요일";
        } else if(lectureDay == 4){
            return "금요일";
        }
        return null;
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
        // 이미 해당 시간에 수강 중인 강의인지 확인
        //student = studentRepository.findById("koo"); // 테스트용 : 추후 로그인 사용자로 대체 예정
        boolean isAlreadyRegistered = student.getLectureRegistrationList().stream()
                .anyMatch(l -> l.getLectureTime() == pickLecture.getLectureTime());

        // 이미 수강 중인 강의가 없으면 수강 신청 처리
        if (!isAlreadyRegistered) {
            LectureRegistration lectureRegistration = new LectureRegistration();
            lectureRegistration.setLectureId(lecture.getLectureId());
            lectureRegistration.setStudentId(student.getId());
            lectureRegistration.setLectureDay(intLectureDayToRealDay(pickLecture.getLectureDay()));
            lectureRegistration.setLectureTime(lecture.getLectureTime());

            student.getLectureRegistrationList().add(lectureRegistration);
            student.getLectureRegistrationIdList().add(lectureRegistration.getId());

            lecture = lectureRepository.findById(choiceLectureId);
            lecture.getLectureRegistrationList().add(lectureRegistration);
            lecture.getLectureRegistrationIdList().add(lectureRegistration.getId());

            lectureRegistrationRepository.insert(lectureRegistration);
            lectureRegistrationRepository.save();

            studentRepository.save();
            lectureRepository.save();
            //studentRegistrations.put(student.getId(), lecture.getLectureRegistrationList());
            System.out.println("수강 신청이 완료되었습니다.");
            student.setLectureCost(student.getLectureCost() + 100000L);
        } else
            System.out.println("이미 해당 시간에 다른 강의를 수강 중입니다.");
    }

    // 수강 취소
    public void deleteLecture() throws IOException {
        //student = studentRepository.findById("koo"); // 테스트용 : 추후 로그인 사용자로 대체 예정
        // 내 시간표 보여주기
        try {
            System.out.println(studentRepository.findById(student.getId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 취소하고자 하는 강의 고르기
        System.out.print("취소하고자 하는 강의를 입력해주세요: ");
        String lectureId = sc.nextLine();

        // 내 시간표에 해당 id 강의가 있는지 확인 후 삭제 또는 취소 실패 문구 출력
        List<LectureRegistration> studentTimetable = student.getLectureRegistrationList();

        boolean isCancelled = false;
        for (LectureRegistration lectureRegistration : studentTimetable) {
            if (lectureRegistration.getLectureId().equals((lectureId))) {
                lectureRegistrationRepository.delete(lectureRegistration);
                lectureRegistrationRepository.save();
                isCancelled = true;
                break;
            }
        }

        if (isCancelled) {
            System.out.println("수강이 취소되었습니다.");
        } else {
            System.out.println("해당 강의를 수강하고 있지 않습니다.");
        }

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
    public void showStudyRoom() {
        //boolean[][] checkSeat = studyRoom.getCheckSeat();
        String reservationInformation = studyRoom.getReservationMap().getOrDefault(student.getId(), "");
        for (int i = 0; i < studyRoom.getCheckSeat().length; i++) {
            for (int j = 0; j < studyRoom.getCheckSeat()[i].length; j++) {
                // 예약된 좌석은 'X', 예약 가능한 좌석은 좌석 번호 표시
                if (studyRoom.getCheckSeat()[i][j]) {
                    // 예약좌석이 내 좌석인지 다른 사람 좌석인지 체크
                    if(reservationInformation.equals((i+1) + "-" + (j+1))) System.out.printf("[%s] ", "내좌석");
                    else System.out.printf("[%s] ", "X");
                } else {
                    System.out.printf("[%d-%d] ", i + 1, j + 1);
                }
            }
            System.out.println(); // 다음 줄로 넘어감
        }
        System.out.println("*****************************************");
    }


    // 좌석 예약
    // 좌석 번호 입력 후 예약 가능 여부 확인
    public boolean isPossibleReserve() {
        System.out.print("예약을 원하는 좌석 번호를 입력하세요(ex: 1-1) : ");
        String choiceSeat = sc.nextLine();
        System.out.println();
        String[] seatInfo = choiceSeat.split("-");  // "-" 떼고 행 열 따로 취급  >  좌석 번호 검증을 위해 만든 배열
        int x = Integer.parseInt(seatInfo[0]) - 1;
        int y = Integer.parseInt(seatInfo[1]) - 1;

        // 좌석 번호의 유효성 검사
        if (x < 0 || x >= studyRoom.getCheckSeat().length || y < 0 || y >= studyRoom.getCheckSeat()[0].length) {
            System.out.println("잘못된 좌석 번호입니다.");
            return false;
        }

        // 좌석이 이미 예약된 경우
        if (studyRoom.getCheckSeat()[x][y]) {
            System.out.println("이미 예약된 좌석입니다.");
            return false;
        }

        // 만약 이미 이 학생의 당일 예약 기록이 있다면
        if (studyRoom.getReservationMap().containsKey(student.getId())) {
            System.out.println("이미 예약한 좌석이 있습니다.");
            System.out.println("예약한 좌석 : " + studyRoom.getReservationMap().get(student.getId()));
            return false;
        }

        // 예약 가능한 경우 (잘못된 좌석 번호 X, 이미 예약된 좌석 X, 당일 예약 기록 X)
        // 해당 좌석 예약
        studyRoom.getCheckSeat()[x][y] = true;
        // 학생 아이디와 좌석 번호 맵에 저장
        studyRoom.getReservationMap().put(student.getId(), String.valueOf(choiceSeat));

        System.out.println("좌석이 성공적으로 예약되었습니다.");

        return true; // 예약 성공

    }

    // 자습실 예약 취소
    public boolean cancelReservation() {
        System.out.println("취소를 원하는 좌석 번호를 입력하세요(ex: 1-1) : ");
        String seatNum = sc.nextLine();

        String[] seatInfo = seatNum.split("-");  // "-" 떼고 행 열 따로 취급  >  좌석 번호 검증을 위해 만든 배열
        int x = Integer.parseInt(seatInfo[0]) - 1;
        int y = Integer.parseInt(seatInfo[1]) - 1;

        // 좌석 번호 유효성 검사
        if (x < 0 || x >= studyRoom.getCheckSeat().length || y < 0 || y >= studyRoom.getCheckSeat()[0].length) {
            System.out.println("잘못된 좌석 번호입니다.");
            return false;
        }

        // 예약된 좌석인지 확인하고 해당 학생의 예약인지 검사
        if (studyRoom.getReservationMap().containsKey(student.getId()) &&  // 내 좌석이 있는지
                studyRoom.getReservationMap().get(student.getId()).equals(seatNum)) {  // 내가 입력한 좌석 번호와 일치하는지
            // 예약 취소
            studyRoom.getReservationMap().remove(student.getId());
            studyRoom.getCheckSeat()[x][y] = false; // 좌석 예약 취소
            System.out.println("좌석 예약이 취소되었습니다.");
            return true;
        } else {
            System.out.println("해당 좌석은 예약되어 있지 않거나 다른 학생이 예약한 좌석입니다.");
            return false;
        }
    }

}
