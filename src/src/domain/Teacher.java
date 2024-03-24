package src.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Teacher  implements Serializable {
    private String id;
    private String password;
    private String name;
    private String gender;
    private String phoneNumber;
    private String birthday;
    private String email;
    private transient List<Lecture> lectureList;
    private List<String> lectureIdList;

    //선택된 학생 수정할 수 있는 정보 출력
    public void editTeacherInformation(){
        System.out.println("<강사수정정보>");
        System.out.println("1. 강사비밀번호: " + password);
        System.out.println("2. 강사이름: " + name);
        System.out.println("3. 강사휴대폰번호: " + phoneNumber);
        System.out.println("4. 강사이메일: " + email);
    }
    //강사의정보
    public void printTeacherInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id);
    }

    public void printDetailTeacherInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id + ", 성별: " + gender + ", 생년월일: " + birthday + ", 휴대폰번호: " + phoneNumber + ", 이메일: " + email);
        System.out.println("[담당강의]");
        for (Lecture lecture : lectureList) {
            System.out.print(lecture.getLectureName() + " ");
        }
    }

}
