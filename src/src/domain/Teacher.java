package src.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
        System.out.println("------------[강사정보수정]------------");
        System.out.println("1. 강사비밀번호: " + password);
        System.out.println("2. 강사이름: " + name);
        System.out.println("3. 강사휴대폰번호: " + phoneNumber);
        System.out.println("4. 강사이메일: " + email);
        System.out.println("------------------------------------");
    }
    //강사의정보
    public void printTeacherInformation(){
        System.out.println("이름: " + name + ", 아이디: " + id);
        System.out.println("**********************************");
    }

    public void printDetailTeacherInformation(){
        System.out.println("---------["+name+" 강사님]"+"---------");
        System.out.println("1. 아이디: " + id);
        System.out.println("2. 성별: " + gender);
        System.out.println("3. 생년월일: " + birthday);
        System.out.println("4. 휴대폰번호: " + phoneNumber);
        System.out.println("5. 이메일: " + email);
        System.out.println("-----------[담당강의]------------");

        if(lectureList.isEmpty()){
            System.out.println("현재 담당하고 있는 강의가 없습니다!");
            return;
        }

        int count = 0;
        for (Lecture lecture : lectureList) {
            count++;
            System.out.print(count + ". " + lecture.getLectureName());
            System.out.println();
        }
    }

}
