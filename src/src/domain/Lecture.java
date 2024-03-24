package src.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Lecture  implements Serializable {
    private String lectureId; //강의아이디
    private String lectureName;//강의이름
    private int lectureDay;//강의요일
    private int lectureTime;//강의시간
    private String lectureTeacherName;//강의를 맡고있는 선생님이름
    private String lectureTeacherId;//강의를 맡고있는 선생님아이디
    private transient List<LectureRegistration> lectureRegistrationList;
    private List<Long> lectureRegistrationIdList;

    public Lecture() {
        lectureRegistrationList = new ArrayList<>();
    }

    public Lecture(String lectureId, String lectureName, int lectureDay, int lectureTime, String lectureTeacherName, String lectureTeacherId) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.lectureDay = lectureDay;
        this.lectureTime = lectureTime;
        this.lectureTeacherName = lectureTeacherName;
        this.lectureTeacherId = lectureTeacherId;
        this.lectureRegistrationIdList = new ArrayList<>();
    }

    public void printLectureInformation(){
        System.out.println("이름: " + lectureName + ", 아이디: " + lectureId);
    }

    public void printDetailLectureInformation(){

        String realLectureDay = "";
        if(lectureDay == 0){
            realLectureDay = "월요일";
        } else if(lectureDay == 1){
            realLectureDay = "화요일";
        }  else if(lectureDay == 2){
            realLectureDay = "수요일";
        }  else if(lectureDay == 3){
            realLectureDay = "목요일";
        } else if(lectureDay == 4){
            realLectureDay = "금요일";
        }

        String realLectureTime = "";
        if(lectureTime == 0){
            realLectureTime = "10:00 ~ 12:00";
        } else if(lectureTime == 1){
            realLectureTime = "13:00 ~ 14:50";
        }  else if(lectureTime == 2){
            realLectureTime = "15:00 ~ 16:50";
        }  else if(lectureTime == 3){
            realLectureTime = "17:00 ~ 19:00";
        }
        System.out.println("이름: " + lectureName + ", 아이디: " + lectureId + ", 강의요일: " + realLectureDay + ", 강의시간: " + realLectureTime +  ", 담당강사: " + lectureTeacherName);
    }
}
