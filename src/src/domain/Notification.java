package src.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data

public class Notification  implements Serializable {
    private int notificationId;
    private int checkCount;
    private String content;
    private String studentId;
    private LocalDateTime date;
}
