package src.service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.management.QueryEval;
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
    private Repository<Notification, String> notificationRepository;
    private Repository<Student, String> studentRepository;
    private Repository<Teacher, String> teacherRepository;
    private StudyRoom studyRoom;
    private Student student;



    public StudentService() throws IOException {
        this.lectureRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTURE);
        this.lectureRegistrationRepository = RepositoryProvider.getInstance().provide(ServiceType.LECTUREREGISTRATION);
        this.notificationRepository = RepositoryProvider.getInstance().provide(ServiceType.NOTIFICATION);
        this.studentRepository = RepositoryProvider.getInstance().provide(ServiceType.STUDENT);
        this.teacherRepository = RepositoryProvider.getInstance().provide(ServiceType.TEACHER);
        this.studyRoom = new StudyRoom();
    }

    public Student getStudent(){
        return student;
    }

    public void setStudent(Student student){
        this.student = student;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 0. 학생으로 로그인 후 첫 페이지인 메뉴 보이기




    // 1. 수강 관리

    // 전체 강의 리스트 출력
    public void showAllLectureList() throws IOException {
        // lectureRepository 에서 findAll() 메서드 호출
        List<Lecture> allLectures = lectureRepository.findAll();
        System.out.println();
        System.out.println("------------------------------------------[수강 가능한 강의 목록]------------------------------------------");
        //System.out.println("-----------------------------------------------------------------------------------------------------");
        // 가져온 강의 목록 처리
        for (Lecture lecture : allLectures) {
            System.out.print("강의 ID: " + lecture.getLectureId() + "     ");
            System.out.print("강의 이름: " + lecture.getLectureName() + "     ");
            System.out.print("강의 요일: " + intLectureDayToRealDay(lecture.getLectureTime()) + "     "); // 숫자 -> 요일
            System.out.print("강의 시간: " + intLectureTimeToRealTime(lecture.getLectureTime()) + "     ");  // 숫자 -> realtime
            System.out.println("강사: " + lecture.getLectureTeacherName() + "     ");
            //System.out.println(); // 강의 사이에 공백 라인 추가
        }
        System.out.println("--------------------------------------------------------------------------------------------------------\n");
    }


    // 수강 신청 내역 가져와 출력
    public boolean showStudentAllRegistrationLecture() throws IOException {
        //수강 신청 내역 가져오기
        //필터링 (해당 학생의 수강 신청 내역)
        List<LectureRegistration> studentLectureRegistration = lectureRegistrationRepository.findAll().stream()
                .filter(registration -> registration.getStudentId().equals(student.getId()))
                .collect(Collectors.toList());

        if(studentLectureRegistration.isEmpty()) {
            System.out.println("수강 신청 내역이 존재하지 않습니다.\n");
            return false;
        }
        // 해당 학생 수강 신청 내역 출력
        //System.out.println("\n[수강 신청 내역]");
        System.out.println("\n---------------------------------------------[수강 신청 내역]-------------------------------------------");
        for(LectureRegistration lectureRegistration : studentLectureRegistration) {
//            System.out.println(
//                    "수강신청ID: " + lectureRegistration.getId() + ", 강의ID: " + lectureRegistration.getLectureId()
//                            + ", 학생ID: " + lectureRegistration.getStudentId() + ", 강의요일: "
//                            + lectureRegistration.getLectureDay() +
//                            ", 강의시간: " + intLectureTimeToRealTime(lectureRegistration.getLectureTime()));
            System.out.print("강의 ID: " + lectureRegistration.getLectureId() + "     ");
            System.out.print("학생 ID: " + lectureRegistration.getStudentId() + "     ");
            System.out.print("강의 요일: " + lectureRegistration.getLectureDay() + "     ");
            System.out.println("강의 시간: " + intLectureTimeToRealTime(lectureRegistration.getLectureTime()) + "     ");
        }
        System.out.println("------------------------------------------------------------------------------------------------------\n");
        return true;
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

    // 강의 시간 숫자를 실제 시간으로 바꿔주는 함수
    // lectureTime을 숫자로 받으면 각 번호에 맞는 시간으로 리턴
    String intLectureTimeToRealTime (int lectureTime) {
        if(lectureTime == 0){
           return "10:00 ~ 12:00";
        } else if(lectureTime == 1){
            return "13:00 ~ 14:50";
        }  else if(lectureTime == 2){
            return "15:00 ~ 16:50";
        }  else if(lectureTime == 3){
            return "17:00 ~ 19:00";
        }
        return null;
    }


    // 수강 신청
    public void registerLecture(String choiceLectureId) throws IOException {


        // 강의 리스트에서 해당 강의 정보 가져오기
        Lecture pickLecture = lectureRepository.findById(choiceLectureId);
        if (pickLecture == null) {
            System.out.println("\n존재하지 않는 강의입니다\n");
            return;
        }
        // 이미 해당 시간에 수강 중인 강의인지 확인
        boolean isAlreadyRegistered = student.getLectureRegistrationList().stream()
                .anyMatch(l -> l.getLectureTime() == pickLecture.getLectureTime());

        // 이미 수강 중인 강의가 없으면 수강 신청 처리
        if (!isAlreadyRegistered) {
            LectureRegistration lectureRegistration = new LectureRegistration();
            //lectureRegistration.setId((long) count++);
            lectureRegistration.setLectureId(pickLecture.getLectureId());
            lectureRegistration.setStudentId(student.getId());
            lectureRegistration.setLectureDay(intLectureDayToRealDay(pickLecture.getLectureDay()));
            lectureRegistration.setLectureTime(pickLecture.getLectureTime());

            lectureRegistrationRepository.insert(lectureRegistration);
            lectureRegistrationRepository.save();

            student.getLectureRegistrationList().add(lectureRegistration);
            student.getLectureRegistrationIdList().add(lectureRegistration.getId());
            student.setLectureCost(student.getLectureCost() + 100000L);

            pickLecture.getLectureRegistrationList().add(lectureRegistration);
            pickLecture.getLectureRegistrationIdList().add(lectureRegistration.getId());

            studentRepository.save();
            lectureRepository.save();
            //studentRegistrations.put(student.getId(), lecture.getLectureRegistrationList());
            System.out.println("수강 신청이 완료되었습니다.\n");

        } else
            System.out.println("이미 해당 시간에 다른 강의를 수강 중입니다.\n");
    }

    // 수강 취소
    public void deleteLecture(String lectureId) throws IOException {
        // 내 시간표에 해당 id 강의가 있는지 확인 후 삭제 또는 취소 실패 문구 출력
        List<LectureRegistration> studentTimetable = student.getLectureRegistrationList();

        boolean isCancelled = false;
        for (LectureRegistration lectureRegistration : studentTimetable) {
            if (lectureRegistration.getLectureId().equals(lectureId)) {
                lectureRegistrationRepository.delete(lectureRegistration);
                lectureRegistrationRepository.save();
                isCancelled = true;
                break;
            }
        }

        if (isCancelled) {
            System.out.println("\n수강이 취소되었습니다.\n");
            student.setLectureCost(student.getLectureCost()-100000L); //수강취소후 강의료 환불
        } else {
            System.out.println("\n해당 강의를 수강하고 있지 않습니다.\n");
        }

    }

    // 자습실 좌석표 출력
    public void showStudyRoom() {
        //boolean[][] checkSeat = studyRoom.getCheckSeat();
        System.out.println("\n************[좌석표]***********");
        //System.out.println("******************************");
        String reservationInformation = studyRoom.getReservationMap().get(student.getId());
        for (int i = 0; i < studyRoom.getCheckSeat().length; i++) {
            for (int j = 0; j < studyRoom.getCheckSeat()[i].length; j++) {
                // 예약된 좌석은 'X', 예약 가능한 좌석은 좌석 번호 표시
                if (studyRoom.getCheckSeat()[i][j]) {
                    // 예약좌석이 내 좌석인지 다른 사람 좌석인지 체크
                    if(reservationInformation!=null &&reservationInformation.equals((i+1) + "-" + (j+1))) System.out.printf("[%s] ", "내좌석");
                    else System.out.printf("[%s] ", "X");
                } else {
                    System.out.printf("[%d-%d] ", i + 1, j + 1);
                }
            }
            System.out.println(); // 다음 줄로 넘어감
        }
        System.out.println("******************************\n");
    }

    public boolean isExistResevation(){
        if (studyRoom.getReservationMap().containsKey(student.getId())) {
            System.out.println("["+student.getName()+"님] 이미 예약된 좌석이 있습니다.\n");
            return true;
        }
        return false;
    }

    // 좌석 예약
    // 좌석 번호 입력 후 예약 가능 여부 확인
    public boolean isPossibleReserve(String choiceSeat) {
        // 이미 예약 기록이 있을 때, '당일 이미 예약된 좌석이 있습니다.' 출력
        System.out.println();
        int x = -1;
        int y = -1;
        try {
            String[] seatInfo = choiceSeat.split("-");  // "-" 떼고 행 열 따로 취급  >  좌석 번호 검증을 위해 만든 배열
            x = Integer.parseInt(seatInfo[0]) - 1;
            y = Integer.parseInt(seatInfo[1]) - 1;
        } catch (NumberFormatException e) {
            System.out.println("잘못된 형식의 좌석 번호입니다.\n");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("좌석 번호가 유효하지 않습니다.\n");
            return false;
        }
        // 좌석 번호의 유효성 검사
        if (x < 0 || x >= studyRoom.getCheckSeat().length || y < 0 || y >= studyRoom.getCheckSeat()[0].length) {
            System.out.println("잘못된 좌석 번호입니다.\n");
            return false;
        }

        // 좌석이 이미 예약된 경우
        if (studyRoom.getCheckSeat()[x][y]) {
            System.out.println("이미 예약된 좌석입니다.\n");
            return false;
        }

        // 만약 이미 이 학생의 당일 예약 기록이 있다면
        if (studyRoom.getReservationMap().containsKey(student.getId())) {
            System.out.println("이미 예약한 좌석이 있습니다.\n");
            System.out.println("예약한 좌석 : " + studyRoom.getReservationMap().get(student.getId()));
            return false;
        }

        // 예약 가능한 경우 (잘못된 좌석 번호 X, 이미 예약된 좌석 X, 당일 예약 기록 X)
        // 해당 좌석 예약
        studyRoom.getCheckSeat()[x][y] = true;
        // 학생 아이디와 좌석 번호 맵에 저장
        studyRoom.getReservationMap().put(student.getId(), String.valueOf(choiceSeat));

        System.out.println("좌석이 성공적으로 예약되었습니다.\n");

        return true; // 예약 성공

    }
//
    // 자습실 예약 취소
    public boolean cancelReservation(String seatNum) {
        int x = -1;
        int y = -1;
        try {
            String[] seatInfo2 = seatNum.split("-");  // "-" 떼고 행 열 따로 취급  >  좌석 번호 검증을 위해 만든 배열
            x = Integer.parseInt(seatInfo2[0]) - 1;
            y = Integer.parseInt(seatInfo2[1]) - 1;
        } catch (NumberFormatException e) {
            System.out.println("\n잘못된 형식의 좌석 번호입니다.\n");
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\n좌석 번호가 유효하지 않습니다.\n");
            return false;
        }


        // 좌석 번호 유효성 검사
        if (x < 0 || x >= studyRoom.getCheckSeat().length || y < 0 || y >= studyRoom.getCheckSeat()[0].length) {
            System.out.println("\n잘못된 좌석 번호입니다.\n");
            return false;
        }

        // 예약된 좌석인지 확인하고 해당 학생의 예약인지 검사
        if (studyRoom.getReservationMap().containsKey(student.getId()) &&  // 내 좌석이 있는지
                studyRoom.getReservationMap().get(student.getId()).equals(seatNum)) {  // 내가 입력한 좌석 번호와 일치하는지
            // 예약 취소
            studyRoom.getReservationMap().remove(student.getId());
            studyRoom.getCheckSeat()[x][y] = false; // 좌석 예약 취소
            System.out.println("\n좌석 예약이 취소되었습니다.\n");
            return true;
        } else {
            System.out.println("\n해당 좌석은 예약되어 있지 않거나 다른 학생이 예약한 좌석입니다.\n");
            return false;
        }
    }

    // 3. 알림함
    //학생알림함 제공
    public void checkNotification() throws IOException {
        System.out.println("-----------[결제알림함]-----------");
        if(notificationRepository.isExist(student.getId())){
            Queue<Notification> notificationQueue = (Queue<Notification>) notificationRepository.findById(student.getId());
            for (Notification notification : notificationQueue) {
                if(notification.getCheckCount() != 0){
                    System.out.println("[읽음] " + "메세지 내용: " + notification.getStudentContent());
                    System.out.println("[잔액]: " + notification.getBalance());
                } else {
                    System.out.println("[안읽음] " + "메세지 내용: " + notification.getStudentContent());
                    System.out.println("[잔액]: " + notification.getBalance());
                    notification.setCheckCount(1);
                }
            }
        } else {
            System.out.println("     [수신된 알림이 없습니다.]          ");
        }
    }

}
