package src.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Notification  implements Serializable {
    private int notificationId;//알림아이디
    private int checkCount;//읽은 카운트 수
    private String adminContent;//학생에게 보여주는 메시지
    private String studentContent;//관리지아게 보여주는 메시지(승인여부)
    private String studentId;//학생 아이디
    private LocalDateTime date;//발송날짜
    private Long balance;//학생계좌잔액
    private int successStudent = 0;
    private int totalStudent = 0;

    public Notification(int notificationId, int checkCount, String adminContent, String studentContent, String studentId, LocalDateTime date, Long balance) {
        this.notificationId = notificationId;
        this.checkCount = checkCount;
        this.adminContent = adminContent;
        this.studentContent = studentContent;
        this.studentId = studentId;
        this.date = date;
        this.balance = balance;
    }
}

//컨텐트 1, 컨텐트2 만들어서 학생용, 관리자용 따로 보여주게하기