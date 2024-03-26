package src.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student  implements Serializable {
    private String id;//학생 아이디
    private String password;//학생 비밀번호
    private String name;//학생 이름
    private String gender;//학생 성별
    private String phoneNumber;//학생 전화번호
    private String birthday;//학생 생년월일
    private String accountNumber;//학생 계좌번호
    private String accountPassword;//학생 계좌비밀번호
    private Long lectureCost;//학원비
    private List<Long> lectureRegistrationIdList;
    private transient List<LectureRegistration> lectureRegistrationList;

    public Student(String id, String password, String name, String gender, String phoneNumber, String birthday, String accountNumber, String accountPassword) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
        this.lectureCost = 5000L;
        this.lectureRegistrationIdList = new ArrayList<>();
    }
    //선택된 학생 수정할 수 있는 정보 출력
    public void editStudentInformation(){
        System.out.println("------------[학생정보수정]-----------");
        System.out.println("1. 학생비밀번호: " + password);
        System.out.println("2. 학생이름: " + name);
        System.out.println("3. 학생휴대폰번호: " + phoneNumber);
        System.out.println("4. 학생계좌비밀번호: " + accountPassword);
        System.out.println("------------------------------------");
    }

    //학생이름, 학생아이디 출력
    public void printStudentInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id );
        System.out.println("**********************************");
    }

    public void printDetailStudentInformation(){
        System.out.println("---------["+name+" 학생님]"+"---------");
        System.out.println("1. 아이디: " + id);
        System.out.println("2. 성별: " + gender);
        System.out.println("3. 생년월일: " + birthday);
        System.out.println("4. 휴대폰번호: " + phoneNumber);
        System.out.println("5. 계좌번호: " + accountNumber);
        System.out.println("--------------------------------");
        System.out.println();
    }

}
