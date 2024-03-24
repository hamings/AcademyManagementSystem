package src.domain;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data

public class Student  implements Serializable {
    private String id;
    private String password;
    private String name;
    private String gender;
    private String phoneNumber;
    private String birthday;
    private String accountNumber;
    private int lectureCost;
    private String accountPassword;
    private List<Long> lectureRegistrationIdList;
    private transient List<LectureRegistration> lectureRegistrationList;

    //선택된 학생 수정할 수 있는 정보 출력
    public void editStudentInformation(){
        System.out.println("<학생수정정보>");
        System.out.println("1. 학생비밀번호: " + password);
        System.out.println("2. 학생이름: " + name);
        System.out.println("3. 학생휴대폰번호: " + phoneNumber);
        System.out.println("4. 학생계좌비밀번호: " + accountPassword);
    }

    //학생이름, 학생아이디 출력
    public void printStudentInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id);
    }

    public void printDetailStudentInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id + ", 성별: " + gender + ", 생년월일: " + birthday + ", 휴대폰번호: " + phoneNumber + "계좌번호: " + accountNumber);
    }

}
